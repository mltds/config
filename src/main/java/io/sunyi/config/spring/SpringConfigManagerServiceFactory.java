package io.sunyi.config.spring;

import io.sunyi.config.service.ConfigManagerService;
import io.sunyi.config.service.ConfigManagerServiceFactory;
import org.springframework.beans.factory.FactoryBean;

public class SpringConfigManagerServiceFactory implements FactoryBean<ConfigManagerService> {

	@Override
	public ConfigManagerService getObject() throws Exception {
		return ConfigManagerServiceFactory.getInstance().getService();
	}

	@Override
	public Class<?> getObjectType() {
		return ConfigManagerService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
