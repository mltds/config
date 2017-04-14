package io.sunyi.config.client.spi.fetcher.impl;

import com.alibaba.fastjson.JSON;
import io.sunyi.config.client.Constants;
import io.sunyi.config.client.Context;
import io.sunyi.config.client.model.ReqModel;
import io.sunyi.config.client.model.ResModel;
import io.sunyi.config.client.spi.balance.BalanceService;
import io.sunyi.config.client.spi.cache.CacheService;
import io.sunyi.config.client.spi.fetcher.FetcherService;
import io.sunyi.config.client.spi.server.ServerService;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.utils.HttpClientBaseUtils;
import io.sunyi.config.commons.utils.HttpResponseWrapper;
import io.sunyi.config.commons.utils.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author sunyi
 */
public class HttpFetcherService implements FetcherService {

    private final String MIME_TYPE = "text/plain";
    private final String CHARSET = "UTF-8";
    private final Integer CONN_TIMEOUT = 1000;
    private final Integer READ_TIMEOUT = 1000;

    private Logger logger = LoggerFactory.getLogger(HttpFetcherService.class);

    private ServerService serverService;

    private CacheService<String, Config> cacheService;

    private BalanceService balanceService;

    private Context context = Context.getInstance();

    @Override
    public Config fetchConfig(String app, String env, String name) throws Exception {

        check(app, env, name);

        ResModel resModel = null;

        // 是否 必须为最新的配置
        Boolean requiredLatest = context.getConfig2Boolean(Constants.C_K_REQUIRED_LATEST);

        String paramInfo = getParamInfo(app, env, name);

        try {
            String url = getUrl();
            String body = getBody(app, env, name);
            HttpResponseWrapper res = HttpClientBaseUtils.post(url, null, body, MIME_TYPE, CHARSET, CONN_TIMEOUT, READ_TIMEOUT);
            if (res.getStatusCode() != 200) {
                Message message = Message.newMessage("从 Config Server 获取配置时，服务器没有正常响应").info("ParamInfo", paramInfo).info("Response", res);
                throw new RuntimeException(message.toString());
            }
            String resBody = res.getContent();
            resModel = JSON.parseObject(resBody, ResModel.class);
        } catch (Exception e) {
            if (requiredLatest) {
                // 异常交给外部处理
                throw e;
            } else {
                Message message = Message.newMessage("从 Config Server 获取配置失败").info("ParamInfo", paramInfo);
                logger.error(message.toString(), e);
            }
        }

        if (resModel == null) {
            // resModel 为 null，意味着从服务器获取信息失败，并且 requiredLatest == false
            // 那么这时候要从缓存中寻找
            String key = getKey(app, env, name);
            return cacheService.get(key);
        }

        Integer code = resModel.getCode();

        if (ResModel.CODE_OK == code) {
            Config config = resModel.getConfig();
            cache(config);
            return config;
        } else if (ResModel.CODE_NOT_MODIFIED == code) {
            Config config = cacheService.get(getKey(app, env, name));
            return config;
        } else if (ResModel.CODE_SERVER_ERROR == code) {
            Message message = Message.newMessage("从 Config Server 获取配置时，服务器发生内部异常").info("ParamInfo", paramInfo).info("ResModel", resModel);
            logger.error(message.toString());
            if (requiredLatest) {
                return null;
            } else {
                // 服务器返回失败，则从缓存里找
                String key = getKey(app, env, name);
                return cacheService.get(key);
            }
        } else if (ResModel.CODE_NOT_FOUND == code) {
            Message message = Message.newMessage("从 Config Server 获取配置时，没有找到对应配置").info("ParamInfo", paramInfo).info("ResModel", resModel);
            logger.error(message.toString());
            cacheService.del(getKey(app, env, name));
            return null;
        } else if (ResModel.CODE_NOT_ENABLED == code) {
            Message message = Message.newMessage("从 Config Server 获取配置时，对应配置没有启用").info("ParamInfo", paramInfo).info("ResModel", resModel);
            cacheService.del(getKey(app, env, name));
            return null;
        } else {
            Message message = Message.newMessage("从  Config Server 获取配置时取得的 code 无法处理的").info("ParamInfo", paramInfo).info("ResModel", resModel);
            throw new RuntimeException(message.toString());
        }
    }

    private String getUrl() {
        List<String> serverAddresses = serverService.getServerAddresses();
        String serverAddress = balanceService.balance(serverAddresses);
        String url = serverAddress + Context.getInstance().getConfig(Constants.C_K_SERVER_HTTP_API_PATH);
        return url;
    }

    private String getBody(String app, String env, String name) {


        String key = app + env + name;
        ReqModel reqModel = new ReqModel();
        reqModel.setApp(app);
        reqModel.setEnv(env);
        reqModel.setName(name);

        Config config = cacheService.get(key);
        if (config != null) {
            reqModel.setVersion(config.getVersion());
        }


        String body = JSON.toJSONString(reqModel);
        return body;
    }

    private void check(String app, String env, String name) {
        if (StringUtils.isBlank(StringUtils.trim(app))) {
            throw new IllegalArgumentException("App 参数不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(env))) {
            throw new IllegalArgumentException("Env 参数不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(name))) {
            throw new IllegalArgumentException("Name 参数不能为空!");
        }
    }

    private void cache(Config config) {
        String app = config.getApp();
        String env = config.getEnv();
        String name = config.getName();
        String key = getKey(app, env, name);

        cacheService.set(key, config);
    }

    private String getParamInfo(String app, String env, String name) {
        return "App:" + app + ",Env:" + env + ",Name:" + name;
    }

    private String getKey(String app, String env, String name) {
        return app + env + name;
    }


}
