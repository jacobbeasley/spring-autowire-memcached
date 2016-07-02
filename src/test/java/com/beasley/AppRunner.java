package com.beasley;

import com.beasley.sample.CachingService;
import net.spy.memcached.MemcachedClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * A simple test to run the test app locally to play around with it
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MemcachedExampleApplication.class)
@WebAppConfiguration
public class AppRunner {

    @Autowired
    MemcachedClient memcachedClient;

    @Autowired
    CachingService cachingService;

    @Test
    public void runTestApp() throws InterruptedException {
        System.out.println("SPRING BOOT APP BOOTED UP. TEST VIA http://localhost:8080/cache and http://localhost:8080/cache/{name}");

        while(true) {
            Thread.sleep(10);
        }
    }

}
