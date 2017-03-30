package io.sunyi.config.server.dao;

import io.sunyi.config.commons.model.ConfigHistory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunyi
 */
@Repository
public class ConfigHistoryDao extends SqlSessionDaoSupport {

    public int insert(ConfigHistory configHistory) {
        return getSqlSession().insert("confighistory.insert", configHistory);
    }

    public List<ConfigHistory> selectByConfigId(Long configId) {

        Assert.notNull(configId);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("configId",configId);
        return getSqlSession().selectList("confighistory.selectByParam", param);
    }

}
