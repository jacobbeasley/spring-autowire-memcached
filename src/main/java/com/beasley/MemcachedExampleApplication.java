package com.beasley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Sample spring boot application to get test cases running
 */
@SpringBootApplication
@Configuration
public class MemcachedExampleApplication {
    /**
     * Boot up spring boot application
     * @param args command line args
     */
    public static void main(final String[] args) {
        SpringApplication.run(MemcachedExampleApplication.class, args);
    }
}

