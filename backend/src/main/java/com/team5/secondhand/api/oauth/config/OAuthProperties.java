package com.team5.secondhand.api.oauth.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
