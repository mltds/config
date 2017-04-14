package io.sunyi.config.commons.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyi
 */
public class JvmCacheComponent implements CacheComponent {

    private Map<String, CacheEntity<Object>> cache = new ConcurrentHashMap<String, CacheEntity<Object>>();

    @Override
    public void set(String key, Object value) {
        set(key, value, null);
    }

    @Override
    public void set(String key, Object value, Long milliseconds) {
        Long expireTime  = null;
        if (milliseconds != null) {
            expireTime = System.currentTimeMillis() + milliseconds;
        }
        cache.put(key, new CacheEntity<Object>(value, expireTime));
    }

    @Override
    public Object get(String key) {
        CacheEntity<Object> objectCacheEntity = cache.get(key);
        if (objectCacheEntity == null) {
            return null;
        }

        if (objectCacheEntity.isExpired()) {
            return null;
        }

        return objectCacheEntity.data;
    }

    /**
     * 这个类请不要方到redis中,现有的方式,对这种有范型的反序列化有问题
     *
     * @param <T>
     */
    private static class CacheEntity<T> {

        // 存入数据
        private T data;

        // 过期时间
        private Long expireTime;


        public CacheEntity(T data, Long expireTime) {
            this.data = data;
            this.expireTime = expireTime;
        }

        /**
         * 判断是否过期
         */
        public boolean isExpired() {
            if (data == null) {
                return true;
            }

            if (expireTime != null && System.currentTimeMillis() > expireTime) {
                return true;
            }
            return false;
        }
    }
}