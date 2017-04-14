package io.sunyi.config.client.spi.balance;

import io.sunyi.config.client.spi.SPI;

import java.util.List;

/**
 * 负载均衡策略服务
 * @author sunyi
 */
public interface BalanceService extends SPI {

    String balance(List<String> serverAddresses);

}
