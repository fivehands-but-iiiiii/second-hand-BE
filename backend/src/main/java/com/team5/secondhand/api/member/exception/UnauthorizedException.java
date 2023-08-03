package com.team5.secondhand.api.member.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends MemberException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
