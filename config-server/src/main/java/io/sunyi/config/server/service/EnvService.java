package io.sunyi.config.server.service;

import io.sunyi.config.commons.model.Env;
import io.sunyi.config.server.dao.EnvDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author sunyi
 */
@Service
public class EnvService {

    @Autowired
    private EnvDao envDao;

    @Autowired
    private UserService userService;

    public Env add(String name) {
        Assert.hasText(name);

        Date now = new Date();

        Env env = new Env();
        env.setName(name);
        env.setCreator(userService.getCurrentUser());
        env.setCreateTime(now);
        env.setModifyTime(now);
        envDao.insert(env);
        return env;
    }


    public boolean hasEnv() {
        int count = envDao.count();
        return count > 0;
    }

    public List<String> allNames() {
        List<String> names = envDao.selectAllName();
        return names;
    }
}
