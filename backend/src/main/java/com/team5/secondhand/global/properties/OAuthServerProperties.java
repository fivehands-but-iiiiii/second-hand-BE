package com.team5.secondhand.global.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "oauth.git-server")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthServerProperties {
    private String tokenUri;
    private String userInfoUri;
}
