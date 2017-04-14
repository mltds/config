package io.sunyi.config.client.spi.cache.impl;

import io.sunyi.config.client.spi.cache.CacheService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyi
 */
public class JvmCacheService<K, V> implements CacheService<K, V> {



    private final Map<K, V> cache = new ConcurrentHashMap<K, V>();

    @Override
    public V get(K k) {
        return cache.get(k);
    }

    @Override
    public void set(K k, V v) {
        cache.put(k, v);
    }

    @Override
    public V del(K k) {
        return cache.remove(k);
    }
}
