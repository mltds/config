package io.sunyi.config.client;

/**
 * @author sunyi
 */
public interface Constants {

    /********************* Config File 配置文件相关***********************/
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


    /********************* Config Key 配置项的Key***********************/
    /**
     * 服务端 HTTP 接口的路径
     */
    String C_K_SERVER_HTTP_API_PATH = "SERVER_HTTP_API_PATH";
    /**
     * 是否必须为最新的配置:
     * <ul>
     * <li>true: 如果没有从 server 端获取或验证过，则报错</li>
     * <li>false: 如果从 server 端获取或验证失败，则使用缓存中的数据</li>
     * </ul>
     */
    String C_K_REQUIRED_LATEST = "REQUIRED_LATEST";

    /**
     * 当没有找到配置时:
     * <ul>
     * <li>true: 抛出异常报错</li>
     * <li>false: 不抛出异常</li>
     * </ul>
     */
    String C_K_REQUIRED = "REQUIRED";

}
