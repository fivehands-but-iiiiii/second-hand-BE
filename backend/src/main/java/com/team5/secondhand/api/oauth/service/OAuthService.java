package com.team5.secondhand.api.oauth.service;

import com.team5.secondhand.api.oauth.config.OAuthLocalProperties;
import com.team5.secondhand.api.oauth.config.OAuthServerProperties;
import com.team5.secondhand.api.oauth.dto.OAuthToken;
import com.team5.secondhand.api.oauth.dto.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class OAuthService {
    private final OAuthLocalProperties env;
    private final OAuthServerProperties requestServer;

    public UserProfile getGithubUser(String code) {
        OAuthToken oAuthToken = getToken(code);
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

    private OAuthToken getToken(String code) {
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
