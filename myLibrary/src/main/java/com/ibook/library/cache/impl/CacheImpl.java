package com.ibook.library.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.ibook.library.cache.Cache;


public class CacheImpl implements Cache {

    private static Logger log = LoggerFactory.getLogger("library");

    private Map<String, Object> localCache = new ConcurrentHashMap<String, Object>();

    private static MemcachedClient memcachedClient;

    private long countInitValue = Integer.MAX_VALUE;

    private CacheImpl() {

    }

    public  MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public  void setMemcachedClient(MemcachedClient memcachedClient) {
        CacheImpl.memcachedClient = memcachedClient;
    }

    public Long decrementAndGet(String key, long num) throws TimeoutException, InterruptedException, MemcachedException {
        return memcachedClient.decr(key, num);
    }

    public void putCount(String key, long num) throws TimeoutException, InterruptedException, MemcachedException {
        memcachedClient.getCounter(key).set(countInitValue + num);
    }

    public Long getCount(String key) throws TimeoutException, InterruptedException, MemcachedException {
        return this.memcachedClient.getCounter(key, countInitValue).get() - countInitValue;
    }

    public Long incrementAndGet(String key, long num) throws TimeoutException, InterruptedException, MemcachedException {
        Long temp = memcachedClient.incr(key, num); // NOTE 注意负数情况 fix
                                                            // me
        return temp;
    }

    public void put(String key, Object value) throws TimeoutException, InterruptedException, MemcachedException {
        memcachedClient.set(key, 0, value);
    }

    public void put(String key, int exp, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
        memcachedClient.set(key, exp, value);
    }

    public void remove(String key) throws TimeoutException, InterruptedException, MemcachedException {
        localCache.remove(key);
        memcachedClient.delete(key);
    }

    public Object removeLocalCache(String key) {
        return localCache.remove(key);
    }

    public Object get(String key) throws TimeoutException, InterruptedException, MemcachedException {
        Object ret = localCache.get(key);
        if (ret != null) {
            return ret;
        }
        ret = memcachedClient.get(key);
        if (ret != null) {
            localCache.put(key, ret);
        }
        return ret;
    }

    public void putToMemcached(String key, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
        memcachedClient.set(key, 0, value);
    }

    public void putToMemcached(String key, int exp, Object value) throws TimeoutException, InterruptedException,
            MemcachedException {
        memcachedClient.set(key, exp, value);
    }

    public Object getFromMemcached(String key) throws TimeoutException, InterruptedException, MemcachedException {
        return memcachedClient.get(key);
    }

    public void clearLocalCache() {
        localCache.clear();
    }

    public Object getFromLocalCache(String key) {
        return localCache.get(key);
    }


}
