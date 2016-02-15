package io.sunyi.config.service.zk;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import io.sunyi.config.listener.ConfigChangeListener;
import org.springframework.util.Assert;

import io.sunyi.config.Constants;
import io.sunyi.config.Utils;
import io.sunyi.config.cache.ConfigCache;
import io.sunyi.config.cache.ConfigCacheFactory;
import io.sunyi.config.data.zk.ZookeeperClient;
import io.sunyi.config.data.zk.ZookeeperClientFactory;
import io.sunyi.config.service.AbstractConfigService;

/**
 * ConfigService 的 Zookeeper 实现.
 * 
 * @author sunyi
 */
public class ZookeeperConfigService extends AbstractConfigService {

	private ZookeeperClient zookeeperClient;
	private ConfigCache configCache;

	// zookeeper 的 URL
	private final String zkUrl;

	// 这个应用在 Zookeeper 中的 path
	private final String appPath;

	public ZookeeperConfigService(String zkUrl, String applicationName) {
		Assert.hasText(zkUrl, "The zkUrl must not be null or empty");
		Assert.hasText(applicationName, "The appliction name must not be null or empty");

		this.zkUrl = zkUrl;
		super.applicationName = applicationName;
		this.appPath = Constants.ZK_ROOT_PATH_NAME + "/" + applicationName;

		// 初始化
		if (zookeeperClient == null) {
			zookeeperClient = ZookeeperClientFactory.getZookeeperClient(zkUrl);
		}

		if (configCache == null) {
			configCache = ConfigCacheFactory.getConfigCache(getApplicationName());
		}

		// 根目录, /configserver
		zookeeperClient.createPersistent(Constants.ZK_ROOT_PATH_NAME);

		// 应用目录, /configserver/${applicationName}
		zookeeperClient.createPersistent(appPath);

		// 应用客户端目录, /configserver/${applicationName}/clients/${ip}
		zookeeperClient.createPersistent(appPath + Constants.ZK_CLIENT_PATH_NAME);
		String ip = Utils.getLocalAddress("UnknownHost");
		zookeeperClient.createEphemeral(appPath + Constants.ZK_CLIENT_PATH_NAME + "/" + ip);

		// 获取配置
		String data = zookeeperClient.getData(appPath);

		// 初始化缓存
		configCache.refreshConfigs(Utils.deserialization(data));
	}

	@Override
	@PreDestroy
	public void destory() {
		zookeeperClient.doClose();
	}

	@Override
	public LinkedHashMap<String, String> getConfigs() {
		return configCache.getConfigs();
	}

	@Override
	public String getConfig(String key) {
		return configCache.getConfig(key);
	}

	@Override
	public void setConfig(String key, String value) {
		// 获取配置
		String data = zookeeperClient.getData(appPath);
		Map<String, String> configs = new HashMap<String, String>(Utils.deserialization(data));
		configs.put(key, value);
		String newData = Utils.serialization(configs);
		zookeeperClient.setData(appPath, newData);
	}

	@Override
	public void setConfigs4Replace(LinkedHashMap<String, String> config) {
		String newData = Utils.serialization(config);
		zookeeperClient.setData(appPath, newData);
	}

	@Override
	public void setConfigs4Add(LinkedHashMap<String, String> configs) {
		LinkedHashMap<String, String> oldConfigs = configCache.getConfigs();
		if (configs != null) {
			oldConfigs.putAll(configs);
		}
		this.setConfigs4Replace(oldConfigs);
	}

	@Override
	public void deleteConfig(String key) {
		String data = zookeeperClient.getData(appPath);
		LinkedHashMap<String, String> configs = Utils.deserialization(data);
		configs.remove(key);
		String newData = Utils.serialization(configs);
		zookeeperClient.setData(appPath, newData);
	}

	@Override
	public void registerConfigListeners(ConfigChangeListener listener) {
		zookeeperClient.registerChangeListener(appPath, new CacheConfigChangeListener(listener, configCache));

	}

	private static class CacheConfigChangeListener implements ConfigChangeListener {

		private ConfigChangeListener listener;
		private ConfigCache configCache;

		public CacheConfigChangeListener(ConfigChangeListener listener, ConfigCache configCache) {
			this.listener = listener;
			this.configCache = configCache;
		}

		/**
		 * 先更新缓存, 在调用真正的 listener.
		 */
		@Override
		public void onChange(Map<String, String> allConfigs) {
			configCache.refreshConfigs(allConfigs);
			listener.onChange(allConfigs);
		}
	}

	/********** get/set **********/
	public String getZkUrl() {
		return zkUrl;
	}
}
