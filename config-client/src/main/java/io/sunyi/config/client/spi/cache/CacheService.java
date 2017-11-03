package io.sunyi.config.client.spi.cache;

import io.sunyi.config.client.spi.SPI;
import io.sunyi.config.commons.model.Config;

/**
 * 缓存服务
 * <p>当在服务端获取数据状态为 {@link io.sunyi.config.commons.model.ResModel#CODE_NOT_MODIFIED}，由缓存服务提供数据</p>
 * <p>当在服务端获取数据失败时，可能由缓存服务提供数据</p>
 * @author sunyi
 */
public interface CacheService extends SPI {

    String buildKey(String app, String env, String name);

    Config get(String key);

    void cache(Config config);

    void set(String key,Config config);

    Config del(String key);

}
