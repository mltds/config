package io.sunyi.config.server.dao;

import io.sunyi.config.commons.model.App;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author sunyi
 */
@Repository
public class AppDao extends SqlSessionDaoSupport {

    /**
     * 只返回 Name 列表
     */
    @SuppressWarnings("unchecked")
    public List<String> selectAllName() {
        return (List<String>) super.getSqlSession().selectList("app.selectAllName");
    }

    public List<App> selectAll() {
        return (List<App>) super.getSqlSession().selectList("app.selectAll");
    }

    public int insert(App app) {
        return super.getSqlSession().insert("app.insert", app);
    }

    public App selectByName(String name) {
        Assert.hasText(name);
        return (App) super.getSqlSession().selectList("app.selectByName", name);
    }


    public int count() {
        return (Integer) super.getSqlSession().selectOne("app.count");
    }


}