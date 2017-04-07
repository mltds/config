package io.sunyi.config.client.api;

import io.sunyi.config.commons.model.Config;

/**
 *
 * @author sunyi
 */
public interface ConfigService {

    /**
     * 应用名称
     * @return
     */
    String getApp();

    /**
     * 环境名称
     * @return
     */
    String getEnv();

    /**
     * 配置名称
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    Config getConfig();

}