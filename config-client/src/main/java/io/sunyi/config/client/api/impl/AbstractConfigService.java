package io.sunyi.config.client.api.impl;

import io.sunyi.config.client.Context;
import io.sunyi.config.client.api.ConfigService;
import io.sunyi.config.client.spi.cache.CacheService;
import io.sunyi.config.client.spi.core.CoreService;
import io.sunyi.config.commons.model.Config;

/**
 * @author sunyi
 */
public abstract class AbstractConfigService<C> implements ConfigService<C> {

    private final String app;
    private final String env;
    private final String name;

    protected Context context;
    protected CacheService cacheService;

    public AbstractConfigService(String app, String env, String name) {
        this.app = app;
        this.env = env;
        this.name = name;

        context = Context.getInstance();
        cacheService = context.getBean(CacheService.class);
    }

    /**
     * 配置描述信息
     * @return
     */
    public String getInfo() {
        return "{" + "app='" + app + '\'' + ", env='" + env + '\'' + ", name='" + name + '\'' + '}';
    }

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public String getEnv() {
        return env;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Config getConfig() {
        String k = cacheService.buildKey(app, env, name);
        return cacheService.get(k);
    }

    @Override
    public String toString() {
        return "AbstractConfigService{" +
                "app='" + app + '\'' +
                ", env='" + env + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}