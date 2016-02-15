package io.sunyi.config.service;

import io.sunyi.config.Constants;
import io.sunyi.config.Utils;
import io.sunyi.config.service.zk.ZookeeperConfigManagerService;

public class ConfigManagerServiceFactory {

	private volatile static ConfigManagerServiceFactory instance = new ConfigManagerServiceFactory();
	private volatile ConfigManagerService configManagerService = null;

	public static ConfigManagerServiceFactory getInstance() {
		return instance;
	}

	public ConfigManagerService getService() {
		if (configManagerService == null) {
			synchronized (this) {
				if (configManagerService == null) {
					String zkUrl = Utils.getConfig(Constants.PRO_CONFIG_ZK_URL_KEY);
					configManagerService = new ZookeeperConfigManagerService(zkUrl);

					Runtime.getRuntime().addShutdownHook(new Thread("ConfigManagerService destory thread") {
						@Override
						public void run() {
							configManagerService.destory();
						}
					});
				}
			}
		}
		return configManagerService;
	}
}
