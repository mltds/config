package io.sunyi.config.service;

import java.util.LinkedHashMap;
import java.util.Map;

import io.sunyi.config.listener.ConfigChangeListener;

/**
 * 客户端使用的Service
 * 
 * @author sunyi
 * 
 */
public interface ConfigService {
	String getApplicationName();

	/**
	 * 
	 * @return 返回 {@link LinkedHashMap} 而不是 {@link Map}, 是为了让使用者知道这个是有序的.
	 */
	LinkedHashMap<String, String> getConfigs();

	String getConfig(String key);

	void setConfig(String key, String value);

	/**
	 * 会先清空已有配置.
	 * 
	 * @param configs
	 *            {@link LinkedHashMap} 有序配置, 可以在管理端的 UI 上更容易操作.
	 */
	void setConfigs4Replace(LinkedHashMap<String, String> configs);

	/**
	 * 会追加配置,不会清空.
	 * 
	 * @param configs
	 *            {@link LinkedHashMap} 有序配置, 可以在管理端的 UI 上更容易操作.
	 */
	void setConfigs4Add(LinkedHashMap<String, String> configs);

	void deleteConfig(String key);

	void registerConfigListeners(ConfigChangeListener listener);

	void destory();

}