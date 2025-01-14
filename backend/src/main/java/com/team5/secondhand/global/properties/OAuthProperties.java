package com.team5.secondhand.global.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
