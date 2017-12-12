package io.sunyi.config.client.test;

import java.io.IOException;

import org.junit.Test;

import io.sunyi.config.client.Context;
import io.sunyi.config.client.Launcher;
import io.sunyi.config.client.api.impl.PlaintextConfigService;
import io.sunyi.config.client.spi.core.CoreService;

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