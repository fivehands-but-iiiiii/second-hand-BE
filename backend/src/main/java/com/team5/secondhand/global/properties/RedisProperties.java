package com.team5.secondhand.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("spring.cache.redis")
public class RedisProperties {

    private final String host;
    private final int port;

    @ConstructorBinding
    public RedisProperties(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
