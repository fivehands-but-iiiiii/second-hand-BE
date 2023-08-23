package com.team5.secondhand.global.dto;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ErrorResponse {
    private final String errorInfo;
    private final String message;

    protected ErrorResponse(String errorInfo, String message) {
        this.errorInfo = errorInfo;
        this.message = message;
    }

    public static ErrorResponse occur (Exception e, String message) {
        return new ErrorResponse(e.toString(), message);
    }

    public static ErrorResponse occur (Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ErrorResponse(e.toString(), e.getMessage());
    }
}
