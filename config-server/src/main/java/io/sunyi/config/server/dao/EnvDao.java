package io.sunyi.config.server.dao;

import io.sunyi.config.commons.model.Env;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sunyi
 */
@Repository
public class EnvDao extends SqlSessionDaoSupport {

    public int insert(Env env) {
        return super.getSqlSession().insert("env.insert", env);
    }

    @SuppressWarnings("unchecked")
    public List<Env> selectAll() {
        return (List<Env>) super.getSqlSession().selectList("env.selectAll");
    }

    public int count() {
        return (Integer) super.getSqlSession().selectOne("env.count");
    }

    @SuppressWarnings("unchecked")
    public List<String> selectAllName() {
        return (List<String>) super.getSqlSession().selectList("env.selectAllName");
    }

}