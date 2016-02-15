package io.sunyi.config.listener;

import java.util.Map;

import io.sunyi.config.service.ConfigService;

/**
 * 配置监听器
 * 
 * @author sunyi
 */
public interface ConfigChangeListener {

	/**
	 * 首次注册时, 调用此方法一次, 传入最新的配置.<br>
	 * 当数据有变化时, 会将新的配置推送过来. 实时性依赖 {@link ConfigService} 实现. <br>
	 * 
	 * @param allConfigs
	 */
	void onChange(Map<String, String> allConfigs);

}