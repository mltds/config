package io.sunyi.config.test.service;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import io.sunyi.config.listener.ConfigChangeListener;
import io.sunyi.config.service.zk.ZookeeperConfigService;

public class ZookeeperConfigServiceTest {

	public static void main(String[] args) throws IOException {
		ZookeeperConfigService service = new ZookeeperConfigService("192.168.1.107:2181", "cstest");

		service.registerConfigListeners(new ConfigChangeListenerTest());

		System.in.read();
	}

	private static class ConfigChangeListenerTest implements ConfigChangeListener {

		@Override
		public void onChange(Map<String, String> allConfigs) {
			System.out.println(JSON.toJSONString(allConfigs, true));

		}
	}

}
