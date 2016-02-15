package io.sunyi.config.service.zk;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import io.sunyi.config.Constants;
import io.sunyi.config.Utils;
import io.sunyi.config.data.zk.ZookeeperClient;
import io.sunyi.config.data.zk.ZookeeperClientFactory;
import io.sunyi.config.service.ConfigManagerService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * ConfigManagerService 的 Zookeeper 实现.
 * 
 * @author sunyi
 */
public class ZookeeperConfigManagerService implements ConfigManagerService {

	private ZookeeperClient zookeeperClient;

	// zookeeper 的 URL
	private volatile String zkUrl;

	public ZookeeperConfigManagerService(String zkUrl) {

		Assert.hasText(zkUrl, "The zkUrl must not be null or empty");

		// 初始化
		if (zookeeperClient == null) {
			zookeeperClient = ZookeeperClientFactory.getZookeeperClient(zkUrl);
		}

		// 根目录, /configserver
		zookeeperClient.createPersistent(Constants.ZK_ROOT_PATH_NAME);

		// 应用客户端目录, /configserver/${applicationName}/clients/${ip}
		zookeeperClient.createPersistent(Constants.ZK_ROOT_PATH_NAME + Constants.ZK_MANAGERS_PATH_NAME);
		String ip = Utils.getLocalAddress("UnknownHost");
		zookeeperClient.createEphemeral(Constants.ZK_ROOT_PATH_NAME + Constants.ZK_MANAGERS_PATH_NAME + "/" + ip);
	}

	@Override
	public List<String> getApplicationNames() {
		List<String> names = zookeeperClient.getChildren(Constants.ZK_ROOT_PATH_NAME);

		// 过滤管理端目录 Constants.ZK_MANAGERS_PATH_NAME
		if (!CollectionUtils.isEmpty(names)) {
			String managerName = Constants.ZK_MANAGERS_PATH_NAME.substring(1);

			for (int i = 0; i < names.size(); i++) {
				if (names.get(i).equals(managerName)) {
					names.remove(i);
				}
			}
		}
		return names;
	}

	@Override
	public List<String> getClients(String applicationName) {
		return zookeeperClient.getChildren(Constants.ZK_ROOT_PATH_NAME + "/" + applicationName + Constants.ZK_CLIENT_PATH_NAME);
	}

	@Override
	public LinkedHashMap<String, String> getConfigs(String applicationName) {
		String data = zookeeperClient.getData(getAppPath(applicationName));
		return Utils.deserialization(data);
	}

	@Override
	public void setConfig(String applicationName, String key, String value) {
		// 获取配置
		String data = zookeeperClient.getData(getAppPath(applicationName));
		Map<String, String> configs = new HashMap<String, String>(Utils.deserialization(data));
		configs.put(key, value);
		String newData = Utils.serialization(configs);
		zookeeperClient.setData(getAppPath(applicationName), newData);
	}

	@Override
	public void setConfigs4Replace(String applicationName, LinkedHashMap<String, String> configs) {
		String newData = Utils.serialization(configs);
		zookeeperClient.setData(getAppPath(applicationName), newData);
	}

	@Override
	public void setConfigs4Add(String applicationName, LinkedHashMap<String, String> configs) {
		LinkedHashMap<String, String> oldConfigs = this.getConfigs(applicationName);
		if(configs != null){
			oldConfigs.putAll(configs);
		}
		this.setConfigs4Replace(applicationName, oldConfigs);
	}

	@Override
	public void deleteConfig(String applicationName, String key) {
		String data = zookeeperClient.getData(getAppPath(applicationName));
		LinkedHashMap<String, String> configs = Utils.deserialization(data);
		configs.remove(key);
		String newData = Utils.serialization(configs);
		zookeeperClient.setData(getAppPath(applicationName), newData);
	}

	public String getZkUrl() {
		return zkUrl;
	}

	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}

	private String getAppPath(String applicationName) {
		return Constants.ZK_ROOT_PATH_NAME + "/" + applicationName;
	}

	@Override
	@PreDestroy
	public void destory() {
		zookeeperClient.doClose();
	}

}
