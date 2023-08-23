package com.team5.secondhand.api.member.exception;

import com.team5.secondhand.api.oauth.dto.UserProfile;
import lombok.Getter;

@Getter
public class UnauthorizedGithubMemberException extends MemberException {

    private UserProfile body;

    public UnauthorizedGithubMemberException(String message, UserProfile body) {
        super(message);
        this.body = body;
    }
}
