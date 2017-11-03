package io.sunyi.config.client.spi.core;

import io.sunyi.config.client.api.ConfigService;
import io.sunyi.config.client.spi.SPI;

/**
 * @author sunyi
 */
public interface CoreService extends SPI {

    /**
     * 注册一个配置服务，默认会刷新一次
     */
    void register(ConfigService configService);

    /**
     * 刷新一个配置服务<br/>
     * 从服务器端获取最新的配置，刷新本地缓存
     */
    void refresh(ConfigService configService);
}