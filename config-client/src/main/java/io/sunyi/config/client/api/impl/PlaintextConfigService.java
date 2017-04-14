package io.sunyi.config.client.api.impl;

import io.sunyi.config.commons.model.Config;

/**
 * @author sunyi
 */
public abstract class PlaintextConfigService extends AbstractConfigService {

    public PlaintextConfigService(String app, String env, String name) {
        super(app, env, name);
    }

    /**
     * 获取最新的配置内容
     */
    public String getContent() {
        Config config = getConfig();
        return config == null ? null : config.getContent();
    }

    /**
     * <ul>
     * <li>第一次加载配置时</li>
     * <li>当配置有变动时</li>
     * </ul>
     * 会调用这个方法
     */
    abstract void callback(String content);

}