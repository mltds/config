package io.sunyi.config.server.dao;

import io.sunyi.config.commons.model.Config;
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
public class ConfigDao extends SqlSessionDaoSupport {

    public int insert(Config config) {
        return getSqlSession().insert("config.insert", config);
    }

    public Config selectById(Long id) {
        Assert.notNull(id);
        return (Config) getSqlSession().selectOne("config.selectById", id);
    }

    @SuppressWarnings("UncheckedExecutionException")
    public List<Config> selectByAppAndEnv(String app, String env) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("app", app);
        map.put("env", env);
        return (List<Config>) super.getSqlSession().selectList("config.selectByAppAndEnv", map);
    }

    public int updateById(Config config) {
        Assert.notNull(config);
        Assert.notNull(config.getId());
        Assert.notNull(config.getEditor());

        // version 自增加一， 在 SQL 层面做
        return getSqlSession().update("config.updateById", config);
    }

    public int delete(Long id) {
        return getSqlSession().delete("config.delete", id);
    }
}
