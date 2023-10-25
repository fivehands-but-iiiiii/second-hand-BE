package com.team5.secondhand.application.oauth.util;

import com.team5.secondhand.application.member.domain.OauthEnv;
import com.team5.secondhand.global.properties.OAuthLocalProperties;
import com.team5.secondhand.global.properties.OAuthProdProperties;
import com.team5.secondhand.global.properties.OAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OAuthProvider {
    private final OAuthProdProperties oAuthProdProperties;
    private final OAuthLocalProperties oAuthLocalProperties;

    public OAuthProperties createOAuthProperties(OauthEnv env) {
        if (env == OauthEnv.PROD) {
            return oAuthProdProperties;
        } else if (env == OauthEnv.LOCAL) {
            return oAuthLocalProperties;
        } else {
            throw new IllegalArgumentException("Unsupported OauthEnv value");
        }
    }
}
