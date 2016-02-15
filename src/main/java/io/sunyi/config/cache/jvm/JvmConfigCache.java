package io.sunyi.config.cache.jvm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.util.Assert;

import io.sunyi.config.cache.ConfigCache;

public class JvmConfigCache implements ConfigCache {

	/**
	 * 因为有 清空-写入 这种2步的原子操作, 所以自己控制读写锁.
	 */
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	// 缓存在应用本地的配置
	private final Map<String, String> configs = new LinkedHashMap<String, String>();

	@Override
	public LinkedHashMap<String, String> getConfigs() {
		rwl.readLock().lock();
		try {
			return new LinkedHashMap(configs);
		} finally {
			rwl.readLock().unlock();
		}
	}

	@Override
	public String getConfig(String key) {
		rwl.readLock().lock();
		try {
			Assert.hasText(key);
			return configs.get(key);
		} finally {
			rwl.readLock().unlock();
		}
	}

	@Override
	public void refreshConfigs(Map<String, String> configs) {
		rwl.writeLock().lock();
		try {
			this.configs.clear();
			this.configs.putAll(configs);
		} finally {
			rwl.writeLock().unlock();
		}
	}

}
