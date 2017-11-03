package io.sunyi.config.client.spi.channel.impl;

import java.net.MalformedURLException;
import java.net.URL;

import com.alibaba.fastjson.JSON;

import io.sunyi.config.client.Constants;
import io.sunyi.config.client.Context;
import io.sunyi.config.client.spi.channel.ChannelService;
import io.sunyi.config.commons.exception.ConfigException;
import io.sunyi.config.commons.model.ReqModel;
import io.sunyi.config.commons.model.ResModel;
import io.sunyi.config.commons.utils.HttpClientBaseUtils;
import io.sunyi.config.commons.utils.HttpResponseWrapper;
import io.sunyi.config.commons.utils.Message;

/**
 * @author sunyi
 */
public class HttpChannelService implements ChannelService {

    private static final String MIME_TYPE = "text/plain";
    private static final String CHARSET = "UTF-8";
    private static final Integer CONN_TIMEOUT = 1000;// 1秒
    private static final Integer READ_TIMEOUT = 1000;
    private static final Integer HTTP_OK = 200;// 不想引用 servlet 包

    @Override
    public ResModel request(ReqModel reqModel) {
        try {
            String url = getUrl();
            String body = getBody(reqModel);
            String resBody;

            HttpResponseWrapper res = HttpClientBaseUtils.post(url, null, body, MIME_TYPE, CHARSET, CONN_TIMEOUT, READ_TIMEOUT);
            if (res.getStatusCode() != HTTP_OK) {
                Message message = Message.newMessage("从 Config Server 获取配置时，服务器没有正常响应").info("ReqModel", reqModel).info("Response", res);
                throw new ConfigException(message.toString());
            }
            resBody = res.getContent();
            return JSON.parseObject(resBody, ResModel.class);
        } catch (Exception e) {
            Message message = Message.newMessage("从 Config Server 获取配置时发生异常").info("ReqModel", reqModel);
            throw new ConfigException(message.toString(), e);
        }
    }

    private String getUrl() throws MalformedURLException {
        Context ctx = Context.getInstance();
        String protocol = ctx.getConfig(Constants.C_K_SERVER_API_PROTOCOL);
        String host = ctx.getConfig(Constants.C_K_SERVER_API_HOST);
        String port = ctx.getConfig(Constants.C_K_SERVER_API_PORT);
        String file = ctx.getConfig(Constants.C_K_SERVER_API_FILE);

        URL url = new URL(protocol, host, Integer.valueOf(port), file);

        return url.toString();
    }

    private String getBody(ReqModel reqModel) {
        String body = JSON.toJSONString(reqModel);
        return body;
    }

}
