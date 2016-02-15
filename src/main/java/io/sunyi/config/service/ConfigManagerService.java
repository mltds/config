package io.sunyi.config.service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 管理端使用的Servie
 * 
 * @author sunyi
 */
public interface ConfigManagerService {

	List<String> getApplicationNames();

	List<String> getClients(String applicationName);

	LinkedHashMap<String, String> getConfigs(String applicationName);

	void setConfig(String applicationName, String key, String value);

	/**
	 * 会先清空已有配置.
	 * 
	 * @param configs
	 *            {@link LinkedHashMap} 有序配置, 可以在管理端的 UI 上更容易操作.
	 */
	void setConfigs4Replace(String applicationName, LinkedHashMap<String, String> configs);

	/**
	 * 会追加配置,不会清空.
	 * 
	 * @param configs
	 *            {@link LinkedHashMap} 有序配置, 可以在管理端的 UI 上更容易操作.
	 */
	void setConfigs4Add(String applicationName, LinkedHashMap<String, String> config);

	void deleteConfig(String applicationName, String key);

	void destory();
}