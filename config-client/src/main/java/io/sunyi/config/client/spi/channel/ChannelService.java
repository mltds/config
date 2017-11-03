package io.sunyi.config.client.spi.channel;

import io.sunyi.config.client.spi.SPI;
import io.sunyi.config.commons.model.ReqModel;
import io.sunyi.config.commons.model.ResModel;

/**
 * 通讯服务
 * 
 * @author sunyi
 */
public interface ChannelService extends SPI {

    ResModel request(ReqModel reqModel);

}