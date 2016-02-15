package io.sunyi.config.spring;

import org.springframework.beans.factory.FactoryBean;

import io.sunyi.config.service.ConfigService;
import io.sunyi.config.service.ConfigServiceFactory;

public class SpringConfigServiceFactory implements FactoryBean<ConfigService> {

	@Override
	public ConfigService getObject() throws Exception {
		return ConfigServiceFactory.getInstance().getService();
	}

	@Override
	public Class<?> getObjectType() {
		return ConfigService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
