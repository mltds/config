package io.sunyi.config.client;

import io.sunyi.config.client.spi.SPI;
import io.sunyi.config.client.spi.cache.impl.MemCacheService;
import io.sunyi.config.commons.exception.ConfigException;
import sun.misc.Unsafe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author sunyi
 */
public class Launcher {

    private static final int STATUS_NOT_STARTED = 0; // 未启动
    private static final int STATUS_STARTING = 1; //启动中
    private static final int STATUS_STARTED = 2;//已启动

    private static volatile int status = STATUS_NOT_STARTED;

    /**
     * Config Client 端启动<br/>
     * 包括加载配置文件，初始化 SPI Bean 等，可重复执行
     * 这个方法是所有事情的第一步
     */
    public static synchronized void start() {
        if (status != STATUS_NOT_STARTED) {
            return;
        }

        status = STATUS_STARTING;

        try {
            // 加载配置文件
            loadConfig();

            // 创建 Bean
            loadBean();

        } catch (Exception e) {
            throw new ConfigException("Config client 启动失败", e);
        }

        status = STATUS_STARTED;

    }

    public static boolean isStarted() {
        return status == STATUS_STARTED;
    }


    private static void loadConfig() throws IOException {
        Properties prop = new Properties();
        loadProperties(prop, Constants.C_F_DEFAULT_NAME);
        loadProperties(prop, Constants.C_F_NAME);
        Set<Map.Entry<Object, Object>> entries = prop.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            Context.getInstance().addConfig(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    private static void loadSpiConfig() throws IOException {
        Properties prop = new Properties();
        loadProperties(prop, Constants.C_F_SPI_DEFAULT_NAME);
        loadProperties(prop, Constants.C_F_SPI_NAME);
        Set<Map.Entry<Object, Object>> entries = prop.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            Context.getInstance().addSpiConfig(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    private static void loadProperties(Properties prop, String resource) throws IOException {
        InputStream is = Launcher.class.getResourceAsStream(resource);
        if (is != null) {
            prop.load(is);
            is.close();
        }
    }


    private static void loadBean() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<Class<?>, Object> beans = new HashMap<Class<?>, Object>();

        //IOC
        loadSpiConfig();

        Map<String, String> spiConfigs = Context.getInstance().getSpiConfigs();
        for (Map.Entry<String, String> spi : spiConfigs.entrySet()) {

            String itf = spi.getKey();//interface
            String ipm = spi.getValue();//implement

            Class<?> itfClass = Class.forName(itf);
            if (!isExtends(itfClass, SPI.class)) {
                continue;
            }

            Class<?> ipmClass = Class.forName(ipm);
            if (!isExtends(ipmClass, itfClass)) {
                continue;
            }

            Object ipmObj = ipmClass.newInstance();
            Context.getInstance().addBean(itfClass, ipmObj);
            beans.put(itfClass, ipmObj);

        }

        //DI
        Set<Class<?>> keys = beans.keySet();
        Collection<Object> values = beans.values();
        for (Object bean : values) {
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field f : declaredFields) {
                if (isExtends(f.getType(), SPI.class)) {
                    boolean hasIpm = false;
                    for (Class<?> key : keys) {
                        if (isExtends(key, f.getType())) {
                            hasIpm = true;
                            if (!f.isAccessible()) {
                                f.setAccessible(true);
                            }
                            f.set(bean, beans.get(key));
                            break;
                        }
                    }
                    if (!hasIpm) {
                        throw new ConfigException("没有找到Bean:" + bean.getClass() + "的属性:" + f.getName() + "的实现");
                    }
                }
            }
        }


    }

    private static boolean isExtends(Class children, Class parent) {

        if (children.equals(parent)) {
            return true;
        }

        Class<?>[] interfaces = children.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class c : interfaces) {
                if (c.equals(parent)) {
                    return true;
                } else if (isExtends(c, parent)) {
                    return true;
                }
            }
        }

        Class superclass = children.getSuperclass();
        if (superclass != null) {
            if (superclass.equals(parent)) {
                return true;
            } else if (isExtends(superclass, parent)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String args[]) {

        System.out.println(isExtends(MemCacheService.class,SPI.class));


    }
}
