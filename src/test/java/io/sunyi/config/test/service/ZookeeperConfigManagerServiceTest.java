package io.sunyi.config.test.service;

import io.sunyi.config.service.zk.ZookeeperConfigManagerService;

public class ZookeeperConfigManagerServiceTest {

	public static void main(String[] args) {
		ZookeeperConfigManagerService service = new ZookeeperConfigManagerService("192.168.1.107:2181");

		service.setConfig("cstest", "a", "a123");
		service.setConfig("cstest", "b", "b123");
		service.setConfig("cstest", "c", "c123");

		service.deleteConfig("cstest", "c");
		service.deleteConfig("cstest", "b");

		service.destory();

	}
}