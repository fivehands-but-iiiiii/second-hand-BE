package com.team5.secondhand.api.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team5.secondhand.api.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfile {
    private String id;
    private String login;
    @JsonProperty("avatar_url")
    private String avatarUrl;

    public Member toMember() {
        return Member.builder()
                .memberId(login)
                .profileImgUrl(avatarUrl)
                .build();
    }
}
