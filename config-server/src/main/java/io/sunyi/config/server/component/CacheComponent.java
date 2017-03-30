package io.sunyi.config.server.component;

/**
 * @author sunyi
 */
public interface CacheComponent {

    void set(String key, Object value);

    /**
     * @param key
     * @param value
     * @param milliseconds  Set the specified expire time, in milliseconds.
     */
    void set(String key, Object value, Long milliseconds);

    Object get(String key);

}
