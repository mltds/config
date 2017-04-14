package io.sunyi.config.client.api.impl;

import io.sunyi.config.client.api.ConfigService;
import io.sunyi.config.commons.model.Config;

/**
 * @author sunyi
 */
public abstract class AbstractConfigService implements ConfigService {

    private final String app;
    private final String env;
    private final String name;

    public AbstractConfigService(String app, String env, String name) {
        this.app = app;
        this.env = env;
        this.name = name;
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
        //TODO
        return null;
    }
}