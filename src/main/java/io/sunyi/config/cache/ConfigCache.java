package io.sunyi.config.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface ConfigCache {

	LinkedHashMap<String, String> getConfigs();

	String getConfig(String key);

	/**
	 * 刷新整个缓存, 将已有配置清空.
	 * 
	 * @param configs
	 */
	void refreshConfigs(Map<String, String> configs);

}
