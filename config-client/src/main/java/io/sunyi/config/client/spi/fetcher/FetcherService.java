package io.sunyi.config.client.spi.fetcher;

import io.sunyi.config.client.spi.SPI;
import io.sunyi.config.commons.model.Config;

/**
 * @author sunyi
 */
public interface FetcherService extends SPI {

    Config fetchConfig(String app, String env, String name) throws Exception;

}