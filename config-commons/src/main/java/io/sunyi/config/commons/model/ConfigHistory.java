package io.sunyi.config.commons.model;

import java.util.Date;

/**
 * @author sunyi
 */
public class ConfigHistory {

    private Long id;

    private Long configId;

    /**
     * 应用名称，是用户可自定义的枚举值，{@link App}
     */
    private String app;

    /**
     * 环境，是用户可自定义的枚举值，{@link Env}
     */
    private String env;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置类型<br>
     * PS: 考虑过是否使用 String 而不使用 enum 防止出现向下兼容问题，但如果使用了新的 type 那么业务系统应该会再次打包，所以应该不会有兼容问题
     */
    private ConfigHistoryType type;
    /**
     * 配置的内容，不同type配置，格式不同
     */
    private String content;

    private ConfigStatus status;

    /**
     * 操作人
     */
    private String operator;
    /**
     * 修改后版本号
     */
    private Integer version;
    /**
     * 操作时间
     */
    private Date operateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

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

    public ConfigHistoryType getType() {
        return type;
    }

    public void setType(ConfigHistoryType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ConfigStatus getStatus() {
        return status;
    }

    public void setStatus(ConfigStatus status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
