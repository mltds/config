package io.sunyi.config.data.zk.zkclient;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.sunyi.config.Constants;
import io.sunyi.config.Utils;
import io.sunyi.config.data.zk.ZookeeperClient;
import io.sunyi.config.listener.ConfigChangeListener;

public class ZkclientZookeeperClient implements ZookeeperClient {

	private Logger logger = LoggerFactory.getLogger(ZkclientZookeeperClient.class);

	private final ZkClient client;

	private volatile KeeperState state = KeeperState.SyncConnected;

	public ZkclientZookeeperClient(String zkUrl) {
		client = new ZkClient(zkUrl);

		client.setZkSerializer(new ZkSerializer() {

			@Override
			public byte[] serialize(Object data) throws ZkMarshallingError {
				if (data == null) {
					return new byte[] {};
				} else {
					try {
						return data.toString().getBytes(Constants.ZK_DATA_CHARSET);
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				}

			}

			@Override
			public Object deserialize(byte[] bytes) throws ZkMarshallingError {
				try {
					if (bytes == null || bytes.length == 0) {
						return null;
					} else {
						return new String(bytes, Constants.ZK_DATA_CHARSET);
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});

	}

	@Override
	public void createPersistent(String path) {
		try {
			client.createPersistent(path, "");
		} catch (ZkNodeExistsException e) {
		}
	}

	@Override
	public void createEphemeral(String path) {
		try {
			client.createEphemeral(path, "");
		} catch (ZkNodeExistsException e) {
		}
	}

	@Override
	public void delete(String path) {
		try {
			client.delete(path);
		} catch (ZkNoNodeException e) {
		}
	}

	@Override
	public List<String> getChildren(String path) {
		return client.getChildren(path);
	}

	@Override
	public boolean isConnected() {
		return state == KeeperState.SyncConnected;
	}

	@Override
	public void doClose() {
		try {
			client.close();
		} catch (Exception e) {
		}
	}

	@Override
	public String getData(String path) {
		return client.readData(path, true);
	}

	@Override
	public void setData(String path, String data) {
		client.writeData(path, data);

	}

	@Override
	public void registerChangeListener(String path, final ConfigChangeListener listener) {
		// 第一次注册监听器时, 触发一次.
		String data = this.getData(path);
		if (data == null || data.toString().length() == 0) {
			listener.onChange(Collections.<String, String> emptyMap());
		} else {
			listener.onChange(Utils.deserialization(data.toString()));
		}

		client.subscribeDataChanges(path, new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				logger.warn("DataDeleted, dataPath:[" + dataPath + "]");
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				if (data == null || data.toString().length() == 0) {
					listener.onChange(Collections.<String, String> emptyMap());
				} else {
					listener.onChange(Utils.deserialization(data.toString()));
				}
			}
		});
	}

}
