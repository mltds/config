package io.sunyi.config.client.spi.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.sunyi.config.client.spi.cache.CacheService;
import io.sunyi.config.commons.model.Config;

/**
 * 基于内存的缓存
 *
 * @author sunyi
 */
public class MemCacheService implements CacheService {

    private final Map<String, Config> cache = new ConcurrentHashMap<String, Config>();

    @Override
    public String buildKey(String app, String env, String name) {
        return app + env + name;
    }

    @Override
    public Config get(String key) {
        return cache.get(key);
    }

    @Override
    public void cache(Config config) {
        String app = config.getApp();
        String env = config.getEnv();
        String name = config.getName();
        String key = buildKey(app, env, name);
        set(key, config);
    }

    @Override
    public void set(String key, Config config) {
        cache.put(key, config);
    }

    @Override
    public Config del(String key) {
        return cache.remove(key);
    }
}
