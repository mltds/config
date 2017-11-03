package io.sunyi.config.commons.model;

import io.sunyi.config.commons.model.Config;

/**
 * @author sunyi
 */
public class ResModel {

    /**
     * 正常返回配置信息
     */
    public static final int CODE_OK = 0;
    /**
     * 版本号没有变动，意味着配置没有变动，可以信赖缓存
     */
    public static final int CODE_NOT_MODIFIED = 1;

    /**
     * 服务器内部错误
     */
    public static final int CODE_SERVER_ERROR = -1;
    /**
     * 没有找到对应的配置
     */
    public static final int CODE_NOT_FOUND = -2;
    /**
     * 对应的配置没有启动
     */
    public static final int CODE_NOT_ENABLED = -3;

    private Integer code;
    private Config config;
    private String errorMessage;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ResModel{" +
                "code=" + code +
                ", config=" + config +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
