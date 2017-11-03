package io.sunyi.config.commons.model;

import com.alibaba.fastjson.JSON;

/**
 * @author sunyi
 */
public class ReqModel {

    private String app;
    private String env;
    private String name;
    private Integer version;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ReqModel{" +
                "app='" + app + '\'' +
                ", env='" + env + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
