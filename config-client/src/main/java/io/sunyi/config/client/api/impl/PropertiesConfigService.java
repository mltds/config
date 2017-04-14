package io.sunyi.config.client.api.impl;

import io.sunyi.config.commons.model.Config;

import java.io.StringReader;
import java.util.Properties;

/**
 * @author sunyi
 */
public abstract class PropertiesConfigService extends AbstractConfigService {

    public PropertiesConfigService(String app, String env, String name) {
        super(app, env, name);
    }

    /**
     * 获取 Properties 配置文件内容
     */
    public Properties getContent() {
        try {
            Properties properties = new Properties();

            Config config = getConfig();
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
                stringReader.close();//只是设置一下null
            }

            return properties;

        } catch (Exception e) {
            throw new RuntimeException(e);
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

    /**
     * <ul>
     * <li>第一次加载配置时</li>
     * <li>当配置有变动时</li>
     * </ul>
     * 会调用这个方法
     */
    abstract void callback(Properties content);

}