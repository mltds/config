package io.sunyi.config.client.spi.core.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.sunyi.config.client.Constants;
import io.sunyi.config.client.Context;
import io.sunyi.config.client.api.ConfigService;
import io.sunyi.config.client.spi.cache.CacheService;
import io.sunyi.config.client.spi.core.CoreService;
import io.sunyi.config.client.spi.channel.ChannelService;
import io.sunyi.config.commons.exception.ConfigException;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.model.ReqModel;
import io.sunyi.config.commons.model.ResModel;
import io.sunyi.config.commons.utils.Message;

/**
 * @author sunyi
 */
public class DefaultCoreService implements CoreService {

    private Logger logger = LoggerFactory.getLogger(DefaultCoreService.class);

    private Context context = Context.getInstance();

    private CacheService cacheService;

    private ChannelService channelService;

    private List<ConfigService> configServices = new CopyOnWriteArrayList();

    public DefaultCoreService() {

        ConfigRefreshThread refreshThread = new ConfigRefreshThread();
        refreshThread.setDaemon(true);
        refreshThread.start();

    }

    @Override
    public void register(ConfigService configService) {
        configServices.add(configService);
        refresh(configService);
    }

    @Override
    public void refresh(ConfigService configService) {
        String app = configService.getApp();
        String env = configService.getEnv();
        String name = configService.getName();

        check(app, env, name);

        // 是否 必须找到配置
        Boolean required = context.getConfig2Boolean(Constants.C_K_REQUIRED);

        try {
            ReqModel reqModel = getReqModel(app, env, name);
            ResModel resModel = channelService.request(reqModel);
            handleResModel(configService, reqModel, resModel);
        } catch (Exception e) {
            if (required) {
                throw e; // 如果必须找到配置,则直接抛出异常
            } else {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private ReqModel getReqModel(String app, String env, String name) {

        String key = app + env + name;
        ReqModel reqModel = new ReqModel();
        reqModel.setApp(app);
        reqModel.setEnv(env);
        reqModel.setName(name);

        Config config = cacheService.get(key);
        if (config != null) {
            reqModel.setVersion(config.getVersion());
        }

        return reqModel;
    }

    private void handleResModel(ConfigService configService, ReqModel reqModel, ResModel resModel) {

        String app = configService.getApp();
        String env = configService.getEnv();
        String name = configService.getName();
        String key = cacheService.buildKey(app, env, name);

        Integer code = resModel.getCode();

        if (ResModel.CODE_OK == code) {
            Config config = resModel.getConfig();
            cacheService.cache(config);

            Object content = configService.extractContent(config);
            configService.callback(content);

            Message message = Message.newMessage("从 Config Server 获取配置").info("Config", config);
            logger.info(message.toString());

        } else if (ResModel.CODE_SERVER_ERROR == code) {
            Message message = Message.newMessage("从 Config Server 获取配置时，服务器发生内部异常").info("ReqModel", reqModel).info("ResModel", resModel);
            throw new ConfigException(message.toString());
        } else if (ResModel.CODE_NOT_FOUND == code) {
            cacheService.del(key);
            Message message = Message.newMessage("从 Config Server 获取配置时，没有找到对应配置").info("ReqModel", reqModel).info("ResModel", resModel);
            throw new ConfigException(message.toString());
        } else if (ResModel.CODE_NOT_ENABLED == code) {
            cacheService.del(key);
            Message message = Message.newMessage("从 Config Server 获取配置时，对应配置没有启用").info("ReqModel", reqModel).info("ResModel", resModel);
            throw new ConfigException(message.toString());
        }

    }

    private void check(String app, String env, String name) {

        if (StringUtils.isBlank(StringUtils.trim(app))) {
            throw new IllegalArgumentException("App 不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(env))) {
            throw new IllegalArgumentException("Env 不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(name))) {
            throw new IllegalArgumentException("Name 不能为空!");
        }
    }

    private class ConfigRefreshThread extends Thread{

        @Override
        public void run() {

            Long interval = context.getConfig2Long(Constants.C_K_PULL_INTERVAL_TIME_MS);

            while (true) {
                try {
                    for (ConfigService service : configServices) {
                        try {
                            refresh(service);
                        } catch (Exception e) {
                            logger.error("从服务器端获取最新的配置时发生异常", e);
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(interval);
                } catch (Exception e) {
                    logger.error("从服务器端获取最新的配置时发生异常", e);
                }
            }
        }

    }
}