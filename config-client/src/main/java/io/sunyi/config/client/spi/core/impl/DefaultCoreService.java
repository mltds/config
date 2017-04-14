package io.sunyi.config.client.spi.core.impl;

import io.sunyi.config.client.api.ConfigService;
import io.sunyi.config.client.spi.cache.CacheService;
import io.sunyi.config.client.spi.core.CoreService;
import io.sunyi.config.client.spi.server.ServerService;
import io.sunyi.config.commons.model.Config;

/**
 * @author sunyi
 */
public class DefaultCoreService implements CoreService {

    private ServerService serverService;

    private CacheService<String, Config> cacheService;


    @Override
    public void registerConfigService(ConfigService configService) {

    }
}
