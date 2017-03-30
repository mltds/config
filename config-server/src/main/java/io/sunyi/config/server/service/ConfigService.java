package io.sunyi.config.server.service;

import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.model.ConfigHistory;
import io.sunyi.config.commons.model.ConfigHistoryType;
import io.sunyi.config.commons.model.ConfigStatus;
import io.sunyi.config.server.dao.ConfigDao;
import io.sunyi.config.server.dao.ConfigHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author sunyi
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigDao configDao;

    @Autowired
    private ConfigHistoryDao configHistoryDao;

    @Autowired
    private UserService userService;


    @Transactional(rollbackFor = Exception.class)
    public Config add(Config config) {

        Assert.notNull(config);
        //TODO 先简单校验下，理论上应该做数据正确性校验
        Assert.hasText(config.getApp());
        Assert.hasText(config.getEnv());
        Assert.hasText(config.getName());
        Assert.notNull(config.getType());

        Date now = new Date();

        // 填充默认值
        if (config.getContent() == null) {
            config.setContent("");
        }
        if (config.getStatus() == null) {
            config.setStatus(ConfigStatus.ENABLED);
        }
        config.setVersion(1);
        config.setCreator(userService.getCurrentUser());
        config.setCreateTime(now);
        config.setEditor(userService.getCurrentUser());
        config.setModifyTime(now);
        configDao.insert(config);


        ConfigHistory configHistory = new ConfigHistory();
        configHistory.setConfigId(config.getId());
        configHistory.setApp(config.getApp());
        configHistory.setEnv(config.getEnv());
        configHistory.setName(config.getName());
        configHistory.setContent(config.getContent());
        configHistory.setType(ConfigHistoryType.CREATED);
        configHistory.setStatus(config.getStatus());
        configHistory.setVersion(config.getVersion());
        configHistory.setOperator(userService.getCurrentUser());
        configHistory.setOperateTime(now);
        configHistoryDao.insert(configHistory);

        return config;
    }

    @Transactional(rollbackFor = Exception.class)
    public Config edit(Config config) {

        Assert.notNull(config);
        Assert.notNull(config.getId());

        Date now = new Date();

        config.setEditor(userService.getCurrentUser());
        config.setModifyTime(now);
        configDao.updateById(config);

        config = configDao.selectById(config.getId());

        ConfigHistory configHistory = new ConfigHistory();
        configHistory.setConfigId(config.getId());
        configHistory.setApp(config.getApp());
        configHistory.setEnv(config.getEnv());
        configHistory.setName(config.getName());
        configHistory.setContent(config.getContent());
        configHistory.setType(ConfigHistoryType.MODIFIED);
        configHistory.setStatus(config.getStatus());
        configHistory.setVersion(config.getVersion());
        configHistory.setOperator(userService.getCurrentUser());
        configHistory.setOperateTime(now);
        configHistoryDao.insert(configHistory);

        return config;

    }

    /**
     * @param configId
     * @return
     */
    public List<ConfigHistory> getHistory(Long configId) {
        return configHistoryDao.selectByConfigId(configId);
    }

}
