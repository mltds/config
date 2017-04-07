package io.sunyi.config.server.dao;

import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.model.ConfigStatus;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

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
    public List<Config> selectByParam(String app, String env) {
        return selectByParam(app, env, null, null);
    }

    public List<Config> selectByParam(String app, String env, String name) {
        return selectByParam(app, env, name, null);
    }

    public List<Config> selectByParam(String app, String env, String name, ConfigStatus status) {
        Config param = new Config();
        param.setApp(app);
        param.setEnv(env);
        param.setName(name);
        param.setStatus(status);
        return (List<Config>) super.getSqlSession().selectList("config.selectByParam", param);
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
