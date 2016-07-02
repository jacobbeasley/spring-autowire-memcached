package com.beasley.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint that caches
 */
@RestController
public class CachingEndpoint {
    @Autowired
    CachingService cachingService;

    @RequestMapping(method = RequestMethod.GET, path = "/cache")
    String cachingEndpoint() throws InterruptedException {
        return cachingService.getFromCache();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cache/{name}")
    String cachingEndpoint(@PathVariable String name) throws InterruptedException {
        return cachingService.getFromCache(name);
    }
}
