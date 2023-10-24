package com.team5.secondhand.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("server.address")
public class ServerProperties {

    private final String domain;

    @ConstructorBinding
    public ServerProperties(String domain) {
        this.domain = domain;
    }
}
