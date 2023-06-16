package com.team5.secondhand.api.member.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends Exception {
    private Object body;

    public UnauthorizedException(String message, Object body) {
        super(message);
        this.body = body;
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
