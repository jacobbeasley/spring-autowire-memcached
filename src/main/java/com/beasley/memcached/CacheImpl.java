package com.beasley.memcached;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;

/**
 * Wrapper of the Memcached client library for Spring Cacheable annotation.
 */
public class CacheImpl implements Cache {
    MemcachedClient memcachedClient;
    String name;
    Integer expiration;
    String prefix;

    /**
     * Implementation of value wrapper for use with our cache implementaiton
     */
    private class ValueWrapperImpl implements ValueWrapper {
        Object object;

        ValueWrapperImpl(Object object) {
            this.object = object;
        }

        @Override
        public Object get() {
            return object;
        }
    }

    /**
     * Instantiate
     * @param memcachedClient a working client to our memcached server(s)
     * @param name Name of cache
     * @param expiration expiration date of things in cache
     * @param prefix prefix to put in front of everything we read/write from cache to prevent intersections of key names
     *               with other services utilizing same memcached servers
     */
    public CacheImpl(MemcachedClient memcachedClient, String name, Integer expiration, String prefix) {
        this.memcachedClient = memcachedClient;
        this.name = name;
        this.expiration = expiration;

        // figure out what to prefix reads/writes with to avoid collisions when writing to memcache
        if (prefix == null) {
            this.prefix = name;
        } else {
            this.prefix = prefix + name;
        }
    }

    /**
     * Convert key object to string and prepend prefix. Used to prepare key for communcation with memcached.
     *
     * @param key Key object
     * @return String representation of key to be used when reading/writing from memcached client
     */
    private String keyName(Object key) {
        return new StringBuilder().append(prefix).append(key.hashCode()).toString();
    }

    /**
     * Get name of this cache
     * @return String name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get Native Memcached Client
     * @return MemcachedClient
     */
    @Override
    public Object getNativeCache() {
        return memcachedClient;
    }

    /**
     * Get an object from the cache
     * @param key Key object
     * @return null if not found, else a value wrapper containing the object we found
     */
    @Override
    public ValueWrapper get(Object key) {
        Object existing = memcachedClient.get(keyName(key));
        return existing == null ? null : new ValueWrapperImpl(existing);
    }

    /**
     * Get a particular object from the cache
     * @param key Key object
     * @param aClass Class type to use
     * @param <T> Class type to use
     * @return the object from cache, null if not found
     */
    @Override
    public <T> T get(Object key, Class<T> aClass) {
        return (T) memcachedClient.get(keyName(key));
    }

    /**
     * Put an object in memcached
     * @param key key object
     * @param value value to put in cache
     */
    @Override
    public void put(Object key, Object value) {
        memcachedClient.add(keyName(key), expiration, value);
    }

    /**
     * Write object in cache if not present, returning previous value
     * @param key key object
     * @param value value to write in cache
     * @return if exists in cache, return existing value. Else, return null. Null indicates it did not existing in cache
     */
    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper existing = get(key);

        if (existing == null) {
            put(key, value);
            return null;
        }

        return existing;
    }

    /**
     * Delete object from cache
     * @param key key object
     */
    @Override
    public void evict(Object key) {
        memcachedClient.delete(keyName(key));
    }

    /**
     * Clear all things in cache. This is NOT recommended with memcached because you if you are sharing with other
     * services, it will clear their caches too.
     */
    @Override
    public void clear() {
        memcachedClient.flush();
    }
}
