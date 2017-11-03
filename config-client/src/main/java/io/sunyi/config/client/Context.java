package io.sunyi.config.client;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.sunyi.config.client.spi.SPI;

/**
 * @author sunyi
 */
public class Context {

    private static Context instance;

    private Map<Class<SPI>, Object> beans = new ConcurrentHashMap<Class<SPI>, Object>();
    private Map<String, String> configs = new ConcurrentHashMap<String, String>();
    private Map<String, String> spiConfigs = new ConcurrentHashMap<String, String>();

    private Context(){}

    public static Context getInstance() {
        if (instance == null) {
            synchronized (Context.class) {
                if (instance == null) {
                    Launcher.start();
                    instance = new Context();
                }
            }
        }
        return instance;
    }

    public String getConfig(String key) {
        return configs.get(key);
    }

    public Boolean getConfig2Boolean(String key) {
        return Boolean.valueOf(getConfig(key));
    }

    public Long getConfig2Long(String key) {
        return Long.valueOf(getConfig(key));
    }

    public Map<String, String> getConfigs() {
        return Collections.unmodifiableMap(configs);
    }

    void addConfig(String key, String value) {
        configs.put(key, value);
    }

    void addConfigs(Map<String, String> configs) {
        this.configs.putAll(configs);
    }

    public String getSpiConfig(String key) {
        return spiConfigs.get(key);
    }

    public Map<String, String> getSpiConfigs() {
        return Collections.unmodifiableMap(spiConfigs);
    }

    void addSpiConfig(String key, String value) {
        spiConfigs.put(key, value);
    }

    void addSpiConfigs(Map<String, String> spiConfigs) {
        this.spiConfigs.putAll(configs);
    }

    public <T extends SPI> T getBean(Class<T> cls) {
        return (T) beans.get(cls);
    }

    void addBean(Class cls, Object bean) {
        beans.put(cls, bean);
    }
}