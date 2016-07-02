package com.beasley.memcached;

import com.beasley.MemcachedExampleApplication;
import com.beasley.sample.CachingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Integration test of Spring using the @Cacheable annotations
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemcachedExampleApplication.class)
@WebAppConfiguration
public class SpringCacheableTest {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	CachingService cachingService;

	@Test
	public void testSavesAndLoadsNoArgs() throws Exception {
		// given: counting times ran query
		cachingService.timesRanQuery = 0;

		// when: run query
		cachingService.getFromCache();
		cachingService.getFromCache();

		// then: run only once
		assert cachingService.timesRanQuery == 1;

		// cleanup
		cacheManager.getCache("shortCache").clear();
	}

	@Test
	public void testSavesAndLoadsWithArgs() throws Exception {
		// given: counting how often query ran
		cachingService.timesRanQuery = 0;

		// when: run query
		cachingService.getFromCache("jacob");
		cachingService.getFromCache("jacob");
		cachingService.getFromCache("jacob");
		cachingService.getFromCache("beasley");
		cachingService.getFromCache("beasley");

		// then: should have only run twice
		assert cachingService.timesRanQuery == 2;

		// cleanup
		cacheManager.getCache("infiniteCache").clear();
	}
}
