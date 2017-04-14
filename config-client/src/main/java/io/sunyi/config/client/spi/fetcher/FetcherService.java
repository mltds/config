package io.sunyi.config.client.spi.fetcher;

import io.sunyi.config.client.spi.SPI;
import io.sunyi.config.commons.model.Config;

/**
 * 获取 Config
 * @author sunyi
 */
public interface FetcherService extends SPI {

    /**
     *
     * @param app
     * @param env
     * @param name
     * @return 获取不到返回 null
     * @throws Exception
     */
    Config fetchConfig(String app, String env, String name) throws Exception;

}