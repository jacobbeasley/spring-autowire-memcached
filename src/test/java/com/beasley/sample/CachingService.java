package com.beasley.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * A sample service that caches things
 */
@Service
public class CachingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static Integer timesRanQuery=0; // used for unit-testing whether cache was utilized or not

    @Cacheable("shortCache")
    public String getFromCache() throws InterruptedException {
        logger.info("Simulating some long-running query...");
        Thread.sleep(1000);
        logger.info("QUERY DONE");
        timesRanQuery++;
        return "Hello World";
    }

    @Cacheable("infiniteCache")
    public String getFromCache(String name) throws InterruptedException {
        logger.info("Simulating some long-running query...");
        Thread.sleep(1000);
        logger.info("QUERY DONE");
        timesRanQuery++;
        return "Hello " + name;
    }
}
