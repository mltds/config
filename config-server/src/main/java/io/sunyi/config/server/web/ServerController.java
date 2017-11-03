package io.sunyi.config.server.web;

import io.sunyi.config.commons.exception.ConfigException;
import io.sunyi.config.commons.model.ReqModel;
import io.sunyi.config.commons.model.ResModel;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.model.ConfigStatus;
import io.sunyi.config.server.dao.ConfigDao;
import io.sunyi.config.server.service.ConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author sunyi
 */
@Controller
@RequestMapping("/server/api")
public class ServerController {

    private Logger logger = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigService configService;

    @ResponseBody
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public ResModel getConfig(@RequestBody ReqModel reqModel) {

        ResModel resModel = new ResModel();

        try {

            checkReqModel(reqModel);

            String app = reqModel.getApp();
            String env = reqModel.getEnv();
            String name = reqModel.getName();
            Integer version = reqModel.getVersion();

            List<Config> configs = configDao.selectByParam(app, env, name);
            if (CollectionUtils.isEmpty(configs)) {
                logger.warn("没有找到对应的配置，请求参数：app:{},env:{},name:{}", new Object[]{app, env, name});
                resModel.setCode(ResModel.CODE_NOT_FOUND);
                return resModel;
            }

            Config config = configs.get(0);//数据库有唯一索引，不会有多条

            if (config.getStatus().equals(ConfigStatus.DISABLED)) {
                logger.warn("找到的配置没有启用，请求参数：app:{},env:{},name:{}", new Object[]{app, env, name});
                resModel.setCode(ResModel.CODE_NOT_ENABLED);
                return resModel;
            }

            if (config.getVersion().equals(version)) {
                resModel.setCode(ResModel.CODE_NOT_MODIFIED);
                return resModel;
            }

            resModel.setCode(ResModel.CODE_OK);
            resModel.setConfig(config);

            return resModel;

        } catch (ConfigException e) {
            logger.warn(e.getMessage());
            resModel.setCode(ResModel.CODE_SERVER_ERROR);
            resModel.setErrorMessage(e.getMessage());
            return resModel;
        } catch (Exception e) {
            logger.error("获取配置时服务器发生内部错误ReqModel:" + reqModel.toString(), e);
            resModel.setCode(ResModel.CODE_SERVER_ERROR);
            resModel.setErrorMessage(e.getMessage());
            return resModel;
        }
    }

    private void checkReqModel(ReqModel reqModel) {

        if (reqModel == null) {
            throw new ConfigException("ReqModel 不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(reqModel.getApp()))) {
            throw new ConfigException("App 参数不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(reqModel.getEnv()))) {
            throw new ConfigException("Env 参数不能为空!");
        }

        if (StringUtils.isBlank(StringUtils.trim(reqModel.getName()))) {
            throw new ConfigException("Name 参数不能为空!");
        }

    }
}