package io.sunyi.config.client.api;

import io.sunyi.config.commons.model.Config;

/**
 *
 * @author sunyi
 */
public interface ConfigService<C> {

    /**
     * 应用名称
     *
     */
    String getApp();

    /**
     * 环境名称
     */
    String getEnv();

    /**
     * 配置名称
     */
    String getName();

    /**
     *
     */
    Config getConfig();

    /**
     * 从Config数据中提取配置内容，有的配置类型需要进行转换
     */
    C extractContent(Config config);

    /**
     * 主动获取配置内容,依赖缓存数据
     */
    C getContent();

    /**
     * 被动通知配置内容
     * <ul>
     * <li>第一次加载配置时</li>
     * <li>当配置有变动时</li>
     * </ul>
     * 会调用这个方法
     */
    void callback(C content);

}