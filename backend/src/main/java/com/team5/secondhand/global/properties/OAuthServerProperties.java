package com.team5.secondhand.global.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "oauth.git-server")
public class OAuthServerProperties {
    private String tokenUri;
    private String userInfoUri;
}
