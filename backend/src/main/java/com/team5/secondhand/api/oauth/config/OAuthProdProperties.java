package com.team5.secondhand.api.oauth.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.env.prod")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthProdProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
