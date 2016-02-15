package io.sunyi.config.data.zk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import io.sunyi.config.data.zk.zkclient.ZkclientZookeeperClient;

public class ZookeeperClientFactory {

	private static final Map<String, ZookeeperClient> clients = new ConcurrentHashMap<String, ZookeeperClient>();

	private static final ReentrantLock lock = new ReentrantLock();

	public static final ZookeeperClient getZookeeperClient(String zkUrl) {
		ZookeeperClient zookeeperClient = clients.get(zkUrl);
		if (zookeeperClient != null) {
			return zookeeperClient;
		} else {
			lock.lock();
			try {
				if (clients.get(zkUrl) != null) {
					return clients.get(zkUrl);
				}

				zookeeperClient = new ZkclientZookeeperClient(zkUrl);
				clients.put(zkUrl, zookeeperClient);
				return zookeeperClient;
			} finally {
				lock.unlock();
			}
		}
	}

}