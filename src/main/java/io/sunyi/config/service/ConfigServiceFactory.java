package io.sunyi.config.service;

import io.sunyi.config.Constants;
import io.sunyi.config.Utils;
import io.sunyi.config.service.zk.ZookeeperConfigService;

/**
 * {@link ConfigService} 工厂类, 默认获取的实现是 {@link ZookeeperConfigService}
 * 
 * @author sunyi
 * 
 */
public class ConfigServiceFactory {

	private static final ConfigServiceFactory instance = new ConfigServiceFactory();
	private volatile ConfigService configService = null;

	public static ConfigServiceFactory getInstance() {
		return instance;
	}

	public ConfigService getService() {
		if (configService == null) {
			synchronized (this) {
				if (configService == null) {
					String zkUrl = Utils.getConfig(Constants.PRO_CONFIG_ZK_URL_KEY);
					String appPath = Utils.getConfig(Constants.PRO_CONFIG_APPLICATION_NAME_KEY);
					configService = new ZookeeperConfigService(zkUrl, appPath);

					Runtime.getRuntime().addShutdownHook(new Thread("ConfigService destory thread") {
						@Override
						public void run() {
							configService.destory();
						}
					});
				}
			}
		}
		return configService;
	}

}
