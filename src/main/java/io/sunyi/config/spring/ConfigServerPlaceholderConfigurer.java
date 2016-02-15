package io.sunyi.config.spring;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.alibaba.fastjson.JSON;
import io.sunyi.config.service.ConfigService;
import io.sunyi.config.service.ConfigServiceFactory;

public class ConfigServerPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Logger logger = LoggerFactory.getLogger(ConfigServerPlaceholderConfigurer.class);

	protected boolean configServerOverride = true;

	private ConfigService configService;

	@Override
	protected Properties mergeProperties() throws IOException {

		Properties result = new Properties();

		configService = ConfigServiceFactory.getInstance().getService();

		Map<String, String> configs = configService.getConfigs();

		Properties mergeProperties = super.mergeProperties();

		if (configServerOverride) {
			result.putAll(mergeProperties);
			result.putAll(configs);
		} else {
			result.putAll(configs);
			result.putAll(mergeProperties);
		}

		logger.info("应用启动时, 获取配置: " + JSON.toJSONString(result, false));
		return result;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public boolean isConfigServerOverride() {
		return configServerOverride;
	}

	/**
	 * ConfigService 是否覆盖已有配置, 默认是true
	 * 
	 * @param configServerOverride
	 */
	public void setConfigServerOverride(boolean configServerOverride) {
		this.configServerOverride = configServerOverride;
	}

}
