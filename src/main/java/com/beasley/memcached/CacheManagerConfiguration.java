package com.beasley.memcached;

import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Memcached cache configuration for spring
 *
 * This autowires in if a memcached client factory bean exists
 */
@Configuration
@ConditionalOnClass( MemcachedClient.class )
@ConditionalOnMissingBean( CacheManager.class )
//@ConditionalOnBean( MemcachedClientFactoryBean.class )
@EnableCaching
public class CacheManagerConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Configure memcached cache manager automatically, adding list of caches from cache properties.
     * @return CacheManager with all memcached caches setup
     */
    @Bean
    CacheManager memcachedCacheManager(MemcachedClient client, MemcachedProperties properties,
           @Value("#{'${spring.cache.cache-names:}'.split(',')}") List<String> springCacheNames) throws Exception {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        if (properties.getCaches() == null) {
            logger.info("MISSING REQUIRED MEMCACHED CONFIGURATION: spring.cache.memcached.caches. " +
                    "Using default spring cache names from spring.cache.cache-names (or empty if not present). ");
            properties.setCaches(springCacheNames.stream().collect(Collectors.toMap(s->s,s->0)));
        }

        List<Cache> caches = properties.getCaches().entrySet().stream()
                .map(entry -> new CacheImpl(client, entry.getKey(), entry.getValue(), properties.getPrefix()))
                .collect(Collectors.toList());

        logger.info("INITIALIZED MEMCACHED CACHE MANAGER WITH CACHES: ");
        for (Map.Entry<String, Integer> entry : properties.getCaches().entrySet()) {
            logger.info(new StringBuilder().append("CACHE ")
                    .append(properties.getPrefix())
                    .append(entry.getKey())
                    .append(" expires ")
                    .append(entry.getValue())
                    .toString());
        }

        cacheManager.setCaches(caches);
        cacheManager.afterPropertiesSet();

        return cacheManager;
    }
}
