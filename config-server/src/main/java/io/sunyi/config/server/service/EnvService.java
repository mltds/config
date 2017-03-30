package io.sunyi.config.server.service;

import io.sunyi.config.server.dao.EnvDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sunyi
 */
@Service
public class EnvService {

    @Autowired
    private EnvDao envDao;

    public boolean hasEnv() {
        int count = envDao.count();
        return count > 0;
    }

    public List<String> allNames() {
        List<String> names = envDao.selectAllName();
        return names;
    }
}
