package io.sunyi.config.service;


public abstract class AbstractConfigService implements ConfigService {

	protected String applicationName;

	@Override
	public String getApplicationName() {
		return this.applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

}