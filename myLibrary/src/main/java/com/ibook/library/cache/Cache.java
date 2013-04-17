package com.ibook.library.cache;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

public interface Cache {

    void put(String key, Object value) throws TimeoutException, InterruptedException, MemcachedException;

    void put(String key, int exp, Object value) throws TimeoutException, InterruptedException, MemcachedException;

    void putCount(String key, long num) throws TimeoutException, InterruptedException, MemcachedException;

    Long getCount(String key) throws TimeoutException, InterruptedException, MemcachedException;

    void putToMemcached(String key, int exp, Object value) throws TimeoutException, InterruptedException,
            MemcachedException;

    void putToMemcached(String key, Object value) throws TimeoutException, InterruptedException, MemcachedException;

    Object get(String key) throws TimeoutException, InterruptedException, MemcachedException;

    Object getFromMemcached(String key) throws TimeoutException, InterruptedException, MemcachedException;

    Long incrementAndGet(String key, long num) throws TimeoutException, InterruptedException, MemcachedException;

    Long decrementAndGet(String key, long num) throws TimeoutException, InterruptedException, MemcachedException;

    void remove(String key) throws TimeoutException, InterruptedException, MemcachedException;

    Object removeLocalCache(String key);

    void clearLocalCache();

    Object getFromLocalCache(String key);

}
