package com.team5.secondhand.api.oauth.config;

import com.team5.secondhand.api.member.domain.OauthEnv;
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
