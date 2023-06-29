package com.team5.secondhand.global.dto;

import lombok.Getter;

@Getter
public class ErrorResponseWithBody<T> extends ErrorResponse {
    private final T body;

    private ErrorResponseWithBody(String errorInfo, String message, T body) {
        super(errorInfo, message);
        this.body = body;
    }

    public static <T> ErrorResponseWithBody<T> occur (Exception e, T body) {
        return new ErrorResponseWithBody<T>(e.toString(), e.getMessage(), body);
    }

}
