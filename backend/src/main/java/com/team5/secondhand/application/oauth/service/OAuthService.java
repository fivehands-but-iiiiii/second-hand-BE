package com.team5.secondhand.application.oauth.service;

import com.team5.secondhand.application.member.domain.OauthEnv;
import com.team5.secondhand.global.properties.OAuthProperties;
import com.team5.secondhand.application.oauth.util.OAuthProvider;
import com.team5.secondhand.global.properties.OAuthServerProperties;
import com.team5.secondhand.application.oauth.dto.OAuthToken;
import com.team5.secondhand.application.oauth.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final OAuthServerProperties requestServer;
    private final OAuthProvider oAuthProvider;

    @Transactional
    public UserProfile getGithubUser(String code, OauthEnv env) {
        OAuthProperties oAuthProperties = oAuthProvider.createOAuthProperties(env);
        OAuthToken oAuthToken = getToken(code, oAuthProperties);
        UserProfile user = getUserProfile(oAuthToken);
        return user;
    }

    private UserProfile getUserProfile(OAuthToken oAuthToken) {

        return WebClient.create()
                .get()
                .uri(requestServer.getUserInfoUri())
                .header("Authorization", oAuthToken.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserProfile.class)
                .block();
    }

    @Transactional
    private OAuthToken getToken(String code, OAuthProperties env) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("client_id", env.getClientId());
        formData.add("client_secret", env.getClientSecret());
        formData.add("redirect_uri", env.getRedirectUri());

        return WebClient.create()
                .post()
                .uri(requestServer.getTokenUri())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(OAuthToken.class)
                .block();
    }
}
