package io.sunyi.config.client.spi.core;

import io.sunyi.config.client.api.ConfigService;
import io.sunyi.config.client.spi.SPI;

/**
 * @author sunyi
 */
public interface CoreService extends SPI {




    void registerConfigService(ConfigService configService);

}
