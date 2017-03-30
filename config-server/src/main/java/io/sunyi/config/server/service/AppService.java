package io.sunyi.config.server.service;

import io.sunyi.config.commons.model.App;
import io.sunyi.config.server.component.CacheComponent;
import io.sunyi.config.server.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author sunyi
 */
@Service
public class AppService {

    @Autowired
    private AppDao appDao;

    @Autowired
    private CacheComponent cacheComponent;

    public App add(String name, String desc, String creator) {
        Assert.hasText(name);
        App app = new App();
        app.setName(name);
        app.setDesc(desc);
        app.setCreator(creator);
        app.setCreateTime(new Date());
        app.setModifyTime(new Date());
        appDao.insert(app);
        return app;
    }

    public List<String> allNames() {
        List<String> names = appDao.selectAllName();
        Collections.sort(names);
        return names;
    }

    public boolean hasApp() {
        int count = appDao.count();
        return count > 0;
    }

}
