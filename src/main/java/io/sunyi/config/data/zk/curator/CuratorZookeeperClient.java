package io.sunyi.config.data.zk.curator;

import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.api.CuratorWatcher;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.RetryNTimes;
import io.sunyi.config.Constants;
import io.sunyi.config.Utils;
import io.sunyi.config.data.zk.ZookeeperClient;
import io.sunyi.config.listener.ConfigChangeListener;

/**
 * @deprecated 这个版本Curator对于 Zookeeper 断线重连机制有些问题.
 * @author sunyi
 */
public class CuratorZookeeperClient implements ZookeeperClient {

	private Logger logger = LoggerFactory.getLogger(CuratorZookeeperClient.class);

	private CuratorFramework client;

	public CuratorZookeeperClient(String url) {
		try {
			Builder builder = CuratorFrameworkFactory.builder().connectString(url).retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
					.connectionTimeoutMs(10000);
			client = builder.build();
			client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
				public void stateChanged(CuratorFramework client, ConnectionState state) {
					if (state == ConnectionState.LOST) {
						logger.warn("Zookeeper ConnectionState.LOST");
						client.start();
					} else if (state == ConnectionState.SUSPENDED) {
						logger.warn("Zookeeper ConnectionState.SUSPENDED");
						client.start();
					} else if (state == ConnectionState.CONNECTED) {
						logger.info("Zookeeper ConnectionState.CONNECTED");
					} else if (state == ConnectionState.RECONNECTED) {
						logger.warn("Zookeeper ConnectionState.RECONNECTED");
					}
				}
			});
			client.start();
		} catch (Exception e) {
			throw new RuntimeException("启动CuratorZookeeperClient失败", e);
		}
		logger.info("CuratorZookeeperClient initialization is complete, url: " + url);
	}

	@Override
	public void createPersistent(String path) {
		try {
			client.create().forPath(path, "".getBytes(Constants.ZK_DATA_CHARSET));
		} catch (NodeExistsException e) {
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void createEphemeral(String path) {
		try {
			client.create().withMode(CreateMode.EPHEMERAL).forPath(path, "".getBytes(Constants.ZK_DATA_CHARSET));
		} catch (NodeExistsException e) {
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(String path) {
		try {
			client.delete().forPath(path);
		} catch (NoNodeException e) {
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}

	@Override
	public void doClose() {
		client.close();
	}

	@Override
	public String getData(String path) {
		try {
			byte[] data = client.getData().forPath(path);
			if (data == null || data.length == 0) {
				return null;
			} else {
				return new String(data, Constants.ZK_DATA_CHARSET);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void setData(String path, String data) {
		try {
			client.setData().forPath(path, data.getBytes(Constants.ZK_DATA_CHARSET));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public List<String> getChildren(String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void registerChangeListener(String path, final ConfigChangeListener listener) {
		try {
			client.getData().usingWatcher(new CuratorWatcher() {

				@Override
				public void process(WatchedEvent event) throws Exception {
					byte[] data = client.getData().usingWatcher(this).forPath(event.getPath());
					try {
						if (data == null || data.length == 0) {
							listener.onChange(Collections.<String, String> emptyMap());
						} else {
							listener.onChange(Utils.deserialization(new String(data, Constants.ZK_DATA_CHARSET)));
						}
					} catch (Exception e) {
						logger.error("process data error", e);
					}

				}
			}).forPath(path);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
