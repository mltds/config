package io.sunyi.config.client.fetcher;

import io.sunyi.config.commons.model.Config;

/**
 *
 * @author sunyi
 */
public interface FetcherService {

    Config fetchConfig(String app, String env, String name);

}