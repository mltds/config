package io.sunyi.config.client;

/**
 * @author sunyi
 */
public interface Constants {

    /********************* Config File 配置文件相关, 都是放在 classpath 下 ***********************/
    /**
     * 常规配置项文件
     */
    String C_F_NAME = "/configclient.properties";//
    /**
     * 默认配置项，内嵌在config client jar 包中
     */
    String C_F_DEFAULT_NAME = "/configclient.default.properties";//

    /**
     * SPI 配置，用户可自定义 SPI 的实现
     */
    String C_F_SPI_NAME = "/configclient.spi.properties";//
    /**
     * 默认的 SPI 配置，内嵌在config client jar 包中
     */
    String C_F_SPI_DEFAULT_NAME = "/configclient.spi.default.properties"; //

    /********************* Config Key 配置项的Key ***********************/
    /**
     * 服务端接口协议，例如：<code>http</code>
     */
    String C_K_SERVER_API_PROTOCOL = "SERVER_API_PROTOCOL";

    /**
     * 服务端接口host地址，例如：<code>192.168.1.11</code>
     */
    String C_K_SERVER_API_HOST = "SERVER_API_HOST";

    /**
     * 服务端接口端口号，例如：<code>8080</code>
     */
    String C_K_SERVER_API_PORT = "SERVER_API_PORT";

    /**
     * 服务端接口路径，例如：<code>/server/config</code>
     */
    String C_K_SERVER_API_FILE = "SERVER_API_FILE";


    /**
     * 当没有找到配置时:
     * <ul>
     * <li>true: 抛出异常报错</li>
     * <li>false: 不抛出异常</li>
     * </ul>
     * 找不到配置的原因有可能是网络问题、Config服务端问题、配置问题等
     */
    String C_K_REQUIRED = "REQUIRED";

    /**
     * 拉取数据的时间间隔
     */
    String C_K_PULL_INTERVAL_TIME_MS = "PULL_INTERVAL_TIME_MS";

}
