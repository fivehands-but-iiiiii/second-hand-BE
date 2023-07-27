package com.team5.secondhand.api.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "oauth.env.prod")
public class OAuthProdProperties extends OAuthProperties {
}
