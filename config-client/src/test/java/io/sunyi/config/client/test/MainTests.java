/**
 * Copyright (C), 2011-2017, 微贷网.
 */
package io.sunyi.config.client.test;

import io.sunyi.config.client.Context;
import io.sunyi.config.client.Launcher;
import io.sunyi.config.client.api.impl.PlaintextConfigService;
import io.sunyi.config.client.api.impl.PropertiesConfigService;
import io.sunyi.config.client.spi.core.CoreService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author sunyi 2017/11/3.
 */


public class MainTests {

    @Test
    public void main() throws IOException {

        Launcher.start();

        Context instance = Context.getInstance();
        CoreService coreService = instance.getBean(CoreService.class);

        String app = "A";
        String env = "TEST";
        String name = "default";
        DefaultConfigService dcs = new DefaultConfigService(app, env, name);

        coreService.register(dcs);

        String content = dcs.getContent();
        System.out.println("getContent---------\n" + content);

        System.in.read();

    }

    public static class DefaultConfigService extends PlaintextConfigService {

        public DefaultConfigService(String app, String env, String name) {
            super(app, env, name);
        }

        @Override
        public void callback(String content) {
            System.out.println("callback---------\n" + content);
        }
    }

}