package io.sunyi.config.client.api.impl;

import java.io.StringReader;
import java.util.Properties;

import io.sunyi.config.commons.exception.ConfigException;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.utils.Message;

/**
 * @author sunyi
 */
public abstract class PropertiesConfigService extends AbstractConfigService<Properties> {

    public PropertiesConfigService(String app, String env, String name) {
        super(app, env, name);
    }

    @Override
    public Properties extractContent(Config config) {
        try {
            Properties properties = new Properties();

            if (config == null) {
                return properties;
            }

            String content = config.getContent();
            if (content == null) {
                return properties;
            }

            StringReader stringReader = new StringReader(content);

            try {
                properties.load(stringReader);
            } finally {
                stringReader.close();// 只是设置一下null
            }

            return properties;

        } catch (Exception e) {
            String message = Message.newMessage("获取 Properties 配置失败").info("ConfigInfo", getInfo()).toString();
            throw new ConfigException(message, e);
        }
    }

    /**
     * 获取 Properties 配置文件内容
     */
    public Properties getContent() {
        try {
            Config config = getConfig();
            return extractContent(config);
        } catch (Exception e) {
            String message = Message.newMessage("获取 Properties 配置失败").info("ConfigInfo", getInfo()).toString();
            throw new ConfigException(message, e);
        }
    }

    /**
     * 获取 Properties 配置文件的某一项配置
     *
     * @param key 文件中配置项的 key
     */
    public String getValue(String key) {
        Object o = getContent().get(key);
        return o == null ? null : o.toString();
    }

}