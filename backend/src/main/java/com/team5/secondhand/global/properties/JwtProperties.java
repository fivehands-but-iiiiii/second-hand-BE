package com.team5.secondhand.global.jwt.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private String claimKey;
    private String authorizationHeader;
    private String tokenType;
    private int expiration;
}
