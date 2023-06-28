package com.team5.secondhand.global.dto;

import lombok.Getter;

@Getter
public class ErrorResponseWithBody extends ErrorResponse {
    private final Object body;

    private ErrorResponseWithBody(String errorInfo, String message, Object body) {
        super(errorInfo, message);
        this.body = body;
    }

    public static ErrorResponseWithBody occur (Exception e, Object body) {
        return new ErrorResponseWithBody(e.toString(), e.getMessage(), body);
    }

}
