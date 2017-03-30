package io.sunyi.config.commons.model;

import java.util.Date;

/**
 * 配置
 *
 * @author sunyi
 */
public class Config {

    private Long id;

    /**
     * 应用名称，是用户可自定义的枚举值，{@link App}，
     * 与 {@link #name}，{@link #env} 组成唯一键
     */
    private String app;

    /**
     * 环境，是用户可自定义的枚举值，{@link Env}
     * 与 {@link #app}，{@link #name}  组成唯一键
     */
    private String env;

    /**
     * 配置名称，与 {@link #app}，{@link #env}  组成唯一键
     */
    private String name;

    /**
     * 配置类型<br>
     * PS: 考虑过是否使用 String 而不使用 enum 防止出现向下兼容问题，但如果使用了新的 type 那么业务系统应该会再次打包，所以应该不会有兼容问题
     */
    private ConfigType type;
    /**
     * 配置的内容，不同type配置，格式不同
     */
    private String content;

    private ConfigStatus status;

    /**
     * 版本号，每次修改自增加一
     */
    private Integer version;

    /**
     * 配置创建人
     */
    private String creator;

    /**
     * 配置修改人（最后一次）
     */
    private String editor;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ConfigType getType() {
        return type;
    }

    public void setType(ConfigType type) {
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", app='" + app + '\'' +
                ", env='" + env + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", version=" + version +
                ", creator='" + creator + '\'' +
                ", editor='" + editor + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
