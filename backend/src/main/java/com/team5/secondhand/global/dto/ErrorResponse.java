package com.team5.secondhand.global.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String errorInfo;
    private final String message;

    private ErrorResponse(String errorInfo, String message) {
        this.errorInfo = errorInfo;
        this.message = message;
    }

    public static ErrorResponse occur (Exception e, String message) {
        return new ErrorResponse(e.toString(), message);
    }

    public static ErrorResponse occur (Exception e) {
        return new ErrorResponse(e.toString(), e.getMessage());
    }
}
