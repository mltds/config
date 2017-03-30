package io.sunyi.config.server.component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by sidawei on 15/11/27.
 *
 * 在分布式部署的使用 用来防止同一台机器上的线程 在缓存失效的时候并发 查询数据库 并发更新缓存<br/>
 * 使用CacheTemplate限制在同一台机器上 只有一个线程可以更新缓存<br/>
 * 同一个CacheTemplate使用统一对读写锁<br/
 * 使用的缓存存储主要是Redis<br/
 */
public class CacheTemplate {

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public <T> T cache(CacheAble<T> cacheAble){
        T data = null;
        readLock.lock();
        try{
            data = cacheAble.getFromCache();
            if (data == null){
                readLock.unlock();
                writeLock.lock();
                try{
                    data = cacheAble.getFromCache(); // 查看是否有其他线程写入了
                    if (data == null){
                        data = cacheAble.getFromSource();
                        if(data != null){
                        	cacheAble.cacheIt(data);
                        }
                    }
                }finally {
                    readLock.lock();
                    writeLock.unlock();
                }
            }
        }finally {
            readLock.unlock();
        }
        return data;
    }

    /**
     * 要被加入缓存逻辑的代码
     * @param <T>
     */
    public interface CacheAble<T>{

        /**
         * 从缓存中查询
         * @return
         */
        T getFromCache();


        /**
         * 从原始位置查询
         * @return
         */
        T getFromSource();

        /**
         * 把查询结果加入缓存中
         * @param data
         * @return
         */
        void cacheIt(T data);

    }


    /**
     * 例子
     */
    public void xx(){
        CacheAble<Object> o = new CacheAble<Object>() {
            @Override
            public Object getFromCache() {
                // 查询redis
                return null;
            }

            @Override
            public Object getFromSource() {
                // 查询数据库
                return null;
            }

            @Override
            public void cacheIt(Object data) {
                // 查询结果加入redis
            }
        };
        // 使用缓存
        // 对同一段业务逻辑应该使用一个CacheTemplate对象,这样才能是同一对读写锁
        Object data = new CacheTemplate().cache(o);
    }

}
