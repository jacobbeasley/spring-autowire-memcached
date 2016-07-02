# spring-autowire-memcached

This library adds autowiring support to the Spring Boot framework.

## Usage

Just add the dependency to maven/gradle, like this:

@TODO: I STILL NEED TO UPLOAD TO MAVEN CENTRAL

Maven:

    <dependency>
      <groupId>com.beasley</groupId>
      <artifactId>memcachedexample</artifactId>
      <version>0.0.1</version>
    </dependency>

Gradle:

    compile 'com.beasley:memcachedexample:0.0.1'

Then, tell spring boot to component scan the "com.beasley.memcached" package

    @SpringBootApplication
    @ComponentScan("com.beasley.memcached")
    public class MyApplication {
        // ...
    }

Finally, just add the appropriate configuration properties in your Spring properties:

    # BASIC CONFIGURATION
    spring.cache.memcached.servers=localhost:11211
    spring.cache.memcached.caches.infiniteCache=0
    spring.cache.memcached.caches.shortCache=60

    # FULL CONFIGURATION OPTIONS
    # spring.cache.memcached.servers=host:port,host2:port2
    # spring.cache.memcached.prefix=someprefix # a prefix when saving to prevent collisions between different services sharing memcached servers
    # spring.cache.memcached.username # memcached username
    # spring.cache.memcached.password # memcached password
    # spring.cache.memcached.cachename=expiration # a cache to store things in. Expiration is expiration time in seconds. See memcached docs for more information.

## Sample App

In the /src/test folder you can see a sample app that is used for my test cases.

## Testing Locally

You will need to first download and run memcached locally on the default port, port 11211. Then, you can run the sample
provided app as a maven test like this...

    ./mvnw test -Dtest=AppRunner

## Integration Testing

You will need to first download and run memcached locally on the default port, port 11211. Then, you can run the
integration tests like this...

./mvnw test

## More information

Checkout
