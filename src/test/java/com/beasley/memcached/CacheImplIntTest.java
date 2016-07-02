package com.beasley.memcached;

import com.beasley.MemcachedExampleApplication;
import com.beasley.sample.CachingService;
import net.spy.memcached.MemcachedClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Integration test of the CacheImpl to make sure its working as expected
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemcachedExampleApplication.class)
@WebAppConfiguration
public class CacheImplIntTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    CachingService cachingService;

    Cache shortCache;

    @Before
    public void setup() {
        shortCache = cacheManager.getCache("shortCache");
    }

    @After
    public void cleanupAfterEach() {
        shortCache.clear();
    }

    @Test
    public void cacheFlush() throws Exception {
        // given: counting times query ran
        cachingService.timesRanQuery = 0;

        // when: flush in between calls
        cachingService.getFromCache();
        shortCache.clear();
        cachingService.getFromCache();

        // then: should rerun
        assert cachingService.timesRanQuery == 2;
    }

    @Test
    public void cacheEvict() throws Exception {
        // when: put something in cache and remove it from cache
        Cache cache = cacheManager.getCache("shortCache");
        cache.put("key", "value");
        cacheManager.getCache("shortCache").evict("key");

        // then: should no longer be in cache
        assert cache.get("key") == null;
    }

    @Test
    public void getName() {
        assert "shortCache".equals(shortCache.getName());
    }

    @Test
    public void getMemcachedClient() {
        assert shortCache.getNativeCache() instanceof MemcachedClient;
    }

    @Test
    public void getByClass() {
        // when: store string in database and load by type
        String value = "VAL";
        shortCache.put("key", value);
        String loaded = shortCache.get("key", String.class);

        // then: should get it
        assert loaded.equals(value);
    }

    @Test
    public void putIfAbsent() {
        // should: return null if doesn't exist yet. Also, save the new value if null.
        assert shortCache.putIfAbsent("key", "value") == null;
        assert "value".equals(shortCache.get("key", String.class));

        // should: return existing value and NOT save new value
        assert "value".equals(shortCache.putIfAbsent("key", "value2").get());
        assert "value".equals(shortCache.get("key").get());
    }

    @Test
    public void putAndGet() {
        // should be able to put and get
        shortCache.put("key", "value");
        assert "value".equals(shortCache.get("key").get());

        // return null if not exists
        assert shortCache.get("unknownKey") == null;
    }
}
