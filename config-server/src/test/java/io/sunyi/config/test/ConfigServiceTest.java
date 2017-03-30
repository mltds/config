package io.sunyi.config.test;

import io.sunyi.config.commons.model.App;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.model.ConfigType;
import io.sunyi.config.commons.model.Env;
import io.sunyi.config.server.dao.AppDao;
import io.sunyi.config.server.dao.EnvDao;
import io.sunyi.config.server.service.ConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;

/**
 * @author sunyi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config-server-context.xml"})
public class ConfigServiceTest {

    @Autowired
    private ConfigService configService;

    @Autowired
    private AppDao appDao;

    @Autowired
    private EnvDao envDao;

    private List<App> apps = null;
    private List<Env> envs = null;

    private Random r = new Random();
    private boolean b = false;

    @Before
    public void before() {
        Assert.notNull(configService);
        apps = appDao.selectAll();
        envs = envDao.selectAll();
    }

    private App getApp() {
        int i = r.nextInt(apps.size());
        return apps.get(i);
    }

    private Env getEnv() {
        int i = r.nextInt(envs.size());
        return envs.get(i);
    }


    @Test
    public void insertTest() {

        for (int i = 0; i < 100; i++) {

            try {
                Config config = new Config();
                config.setName("ConfigName" + r.nextInt(10000));
                config.setApp(getApp().getName());
                config.setEnv(getEnv().getName());

                if (b) {
                    config.setType(ConfigType.PROPERTIES);
                    config.setContent("jdbc.druid.url=jdbc:mysql://db.mysql.com:3306/config?useUnicode=true&characterEncoding=GBK\n" +
                            "jdbc.druid.user=\n" +
                            "jdbc.druid.password=\n" +
                            "jdbc.druid.filters=stat\n" +
                            "jdbc.druid.maxActive=20\n" +
                            "jdbc.druid.initialSize=1\n" +
                            "jdbc.druid.maxWait=60000\n" +
                            "jdbc.druid.minIdle=1\n" +
                            "jdbc.druid.timeBetweenEvictionRunsMillis=60000\n" +
                            "jdbc.druid.minEvictableIdleTimeMillis=300000\n" +
                            "jdbc.druid.validationQuery=SELECT 'x'\n" +
                            "jdbc.druid.testWhileIdle=true\n" +
                            "jdbc.druid.testOnBorrow=false\n" +
                            "jdbc.druid.testOnReturn=false\n" +
                            "jdbc.druid.poolPreparedStatements=true\n" +
                            "jdbc.druid.maxOpenPreparedStatements=20");

                } else {
                    config.setType(ConfigType.PLAINTEXT);
                    config.setContent("ConfigServiceTest insert method");
                }
                b = !b;
                configService.add(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}