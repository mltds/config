package io.sunyi.config.data.zk;

import java.util.List;

import io.sunyi.config.listener.ConfigChangeListener;

public interface ZookeeperClient {

	void createPersistent(String path);

	void createEphemeral(String path);

	List<String> getChildren(String path);

	void delete(String path);

	String getData(String path);

	void setData(String path, String data);

	void registerChangeListener(String path, ConfigChangeListener listener);

	boolean isConnected();

	void doClose();

}
