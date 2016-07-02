package com.beasley.memcached;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Memcached Cache Properties POJO. This autowires and exposes a MemcachedProperties bean if spring.cache.memcached.servers
 * is set
 */
@Component
@ConfigurationProperties(prefix = "spring.cache.memcached")
@ConditionalOnProperty("spring.cache.memcached.servers")
public class MemcachedProperties {
    /**
     * List of cache servers (required). For example: localhost:11211,otherhost:11211
     */
    String servers;

    /**
     * Prefix to avoid conflicts with other services in a distributed-cache environment
     */
    String prefix="";

    /**
     * Optional Memcached auth credentials
     */
    String username;
    String password;

    /**
     * Map of caches and their expiration dates. If none specified, will fall back to spring.cache.cache-names with
     * no expiration date. If none are set there, then it falls back to just using a cache with a name of "" and no
     * expiration date. An expiration time 0 means to never expire. A number under 30 days of seconds will be a number
     * of seconds until it expires. A number greater than 30 days of seconds will be a specific unix timestamp.
     */
    Map<String, Integer> caches;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Integer> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, Integer> caches) {
        this.caches = caches;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
