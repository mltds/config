package io.sunyi.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

public class Utils {

	/**
	 * 是否
	 */
	private static volatile boolean isConfigInitOver = false;
	private static Properties config = new Properties();

	/**
	 * 使用 JSON 序列化, 因为配置的量一般不大
	 * 
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, String> deserialization(String data) {
		if (StringUtils.isBlank(data)) {
			return new LinkedHashMap<String, String>(0);
		}
		return JSON.parseObject(data, LinkedHashMap.class);
	}

	public static String serialization(Map<String, String> config) {
		if (CollectionUtils.isEmpty(config)) {
			return "";
		} else {
			return JSON.toJSONString(config);
		}
	}

	public static String getLocalAddress(String defaultValue) {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * 获取网卡地址
	 * 
	 * @return
	 */
	public static List<String> getAllMacAddresses() {
		List<String> addresses = new ArrayList<String>();

		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = networkInterfaces.nextElement();
				byte[] mac = netInterface.getHardwareAddress();
				if (mac != null)
					sb.delete(0, sb.length());
				for (byte b : mac) {
					String hexString = Integer.toHexString(b & 0xFF);
					sb.append((hexString.length() == 1) ? "0" + hexString : hexString);
				}
				addresses.add(sb.toString());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		return addresses;
	}

	public static String getConfig(String key) {
		return getConfig(key, null);
	}

	public static String getConfig(String key, String defaultValue) {
		try {
			if (!isConfigInitOver) {
				synchronized (config) {
					if (!isConfigInitOver) {
						InputStream resourceAsStream = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream(Constants.PRO_CONFIG_PATH);

						Properties p = new Properties();
						p.load(resourceAsStream);
						config = p;
						isConfigInitOver = true;
					}
				}
			}
			return config.getProperty(key) == null ? defaultValue : config.getProperty(key);
		} catch (IOException e) {
			throw new RuntimeException("ConfigService 初始化失败, 加载配置文件失败.", e);
		}
	}

}
