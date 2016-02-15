package io.sunyi.config.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import io.sunyi.config.cache.jvm.JvmConfigCache;

public class ConfigCacheFactory {

	private static final Map<String, ConfigCache> clients = new ConcurrentHashMap<String, ConfigCache>();

	private static final ReentrantLock lock = new ReentrantLock();

	public static final ConfigCache getConfigCache(String applicationName) {
		ConfigCache configCache = clients.get(applicationName);
		if (configCache != null) {
			return configCache;
		} else {
			lock.lock();
			try {
				if (clients.get(applicationName) != null) {
					return clients.get(applicationName);
				}

				configCache = new JvmConfigCache(); // default JVM Config Cache
				clients.put(applicationName, configCache);
				return configCache;
			} finally {
				lock.unlock();
			}
		}
	}

}
