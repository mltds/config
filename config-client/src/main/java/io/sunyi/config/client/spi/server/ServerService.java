package io.sunyi.config.client.spi.server;

import io.sunyi.config.client.spi.SPI;

import java.util.List;

/**
 * @author sunyi
 */
public interface ServerService extends SPI {

    List<String> getServerAddresses();

}