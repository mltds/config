package io.sunyi.config.client.spi.balance.impl;

import io.sunyi.config.client.spi.balance.BalanceService;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * @author sunyi
 */
public class RandomBalanceService implements BalanceService {

    private final Random random = new Random();

    @Override
    public String balance(List<String> serverAddresses) {

        if (CollectionUtils.isEmpty(serverAddresses)) {
            return null;
        }

        int size = serverAddresses.size();
        if (size == 1) {
            return serverAddresses.get(0);
        }

        int i = random.nextInt(size);
        return serverAddresses.get(i);
    }
}