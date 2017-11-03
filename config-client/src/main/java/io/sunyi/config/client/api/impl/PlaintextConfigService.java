package io.sunyi.config.client.api.impl;

import io.sunyi.config.commons.exception.ConfigException;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.utils.Message;

/**
 * @author sunyi
 */
public abstract class PlaintextConfigService extends AbstractConfigService<String> {

    public PlaintextConfigService(String app, String env, String name) {
        super(app, env, name);
    }


    @Override
    public String extractContent(Config config) {
        return config == null ? null : config.getContent();
    }

    @Override
    public String getContent() {
        try {
            Config config = getConfig();
            return extractContent(config);
        } catch (Exception e) {
            String message =
                    Message.newMessage("获取 Plaintext 配置失败")
                            .info("ConfigInfo", getInfo())
                            .toString();
            throw new ConfigException(message, e);
        }
    }

}