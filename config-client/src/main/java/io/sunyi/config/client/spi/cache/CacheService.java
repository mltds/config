package io.sunyi.config.client.spi.cache;

import io.sunyi.config.client.spi.SPI;

/**
 * @author sunyi
 */
public interface CacheService<K, V> extends SPI {

    V get(K k);

    void set(K k, V v);

    V del(K k);

}
