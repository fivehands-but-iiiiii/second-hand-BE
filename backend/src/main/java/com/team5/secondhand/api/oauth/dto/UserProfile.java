package com.team5.secondhand.api.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfile {
    private String id;
    private String login;
    private String avatar_url;
}
