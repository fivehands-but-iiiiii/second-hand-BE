package com.team5.secondhand.api.oauth.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.env.local")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthLocalProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
