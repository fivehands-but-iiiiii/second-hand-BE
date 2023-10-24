package com.team5.secondhand.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private String claimKey;
    private String authorizationHeader;
    private String tokenType;
    private int expiration;

    @ConstructorBinding
    public JwtProperties(String secretKey, String claimKey, String authorizationHeader, String tokenType, int expiration) {
        this.secretKey = secretKey;
        this.claimKey = claimKey;
        this.authorizationHeader = authorizationHeader;
        this.tokenType = tokenType;
        this.expiration = expiration;
    }
}
