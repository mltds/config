package io.sunyi.config;

public interface Constants {

	/********** properties 配置 **********/
	public static final String PRO_CONFIG_PATH = "config.properties";
	public static final String PRO_CONFIG_ZK_URL_KEY = "configserver.zookeeper.url";
	public static final String PRO_CONFIG_APPLICATION_NAME_KEY = "configserver.applicationname";

	/**
	 * 当使用 zk 做为配置数据源时, zk 的根目录名称
	 */
	public static final String ZK_ROOT_PATH_NAME = "/configserver";

	/**
	 * 这个目录用于这个应用有多少个客户端, 比如 Test 这个应用有3个客户端 A/B/C, 那么
	 * /configserver/Test/clients/ 下面有3个临时目录 A/B/C
	 * 
	 */
	public static final String ZK_CLIENT_PATH_NAME = "/clients";

	/**
	 * 这个目录用于有多少个管理端, 有1个端 D, 那么 /configserver/managers/ 下面有1个临时目录 D
	 * 
	 */
	public static final String ZK_MANAGERS_PATH_NAME = "/managers";

	/**
	 * Zookeeper 数据的编码
	 */
	public static final String ZK_DATA_CHARSET = "UTF-8";
}
