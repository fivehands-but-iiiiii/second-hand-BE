package com.team5.secondhand.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth.env.local")
public class OAuthLocalProperties extends OAuthProperties {
}
