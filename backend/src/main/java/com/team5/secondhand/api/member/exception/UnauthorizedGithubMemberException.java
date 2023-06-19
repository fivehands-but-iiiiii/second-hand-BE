package com.team5.secondhand.api.member.exception;

import lombok.Getter;

@Getter
public class UnauthorizedGithubMemberException extends MemberException {

    private Object body;

    public UnauthorizedGithubMemberException(String message, Object body) {
        super(message);
        this.body = body;
    }
}
