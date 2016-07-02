package com.beasley.memcached;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure memcached connection if not present. Autowires in if spring.cache.memcached.servers is set in properties.
 */
@Configuration
@ConditionalOnClass( MemcachedClient.class )
@ConditionalOnMissingBean( MemcachedClientFactoryBean.class )
@ConditionalOnBean( ConnectionConfiguration.class )
public class ConnectionConfiguration {

    /**
     * Configure Memcached Connection factory AUTOMATICALLY
     * @param properties properties autofilled from spring.cache.memcached
     * @return a factory for connecting to memcached
     * @throws Exception if are missing the required memcached configuration properties
     */
    @Bean
    MemcachedClientFactoryBean memcachedClientFactoryBean(MemcachedProperties properties) throws Exception {
        MemcachedClientFactoryBean bean = new MemcachedClientFactoryBean();

        bean.setServers(properties.getServers());

        if (properties.getUsername() != null) {
            bean.setAuthDescriptor(AuthDescriptor.typical(properties.getUsername(), properties.getPassword()));
        }

        bean.afterPropertiesSet();
        return bean;
    }
}
