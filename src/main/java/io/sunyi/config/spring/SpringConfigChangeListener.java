package io.sunyi.config.spring;

import org.springframework.beans.factory.InitializingBean;

import io.sunyi.config.listener.ConfigChangeListener;
import io.sunyi.config.service.ConfigService;
import io.sunyi.config.service.ConfigServiceFactory;

public abstract class SpringConfigChangeListener implements ConfigChangeListener, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		ConfigService service = ConfigServiceFactory.getInstance().getService();
		service.registerConfigListeners(this);
	}

}
