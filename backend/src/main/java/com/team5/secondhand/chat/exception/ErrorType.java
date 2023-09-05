package com.team5.secondhand.chat.exception;

public enum ErrorType {
    UNAUTHORIZED("unauthorized", "채팅을 연결할 수 없습니다. 로그인을 다시 시도해주세요."),
    BAD_REQUEST("bad request", "요청이 잘못되었습니다."),
    DEFAULT("default", "기존 stomp 에러 타입");

    private final String message;
    private final String errorMessage;


    ErrorType(String message, String errorMessage) {
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static ErrorType of(String message) {
        for (ErrorType errorType : ErrorType.values()) {
            if (errorType.message.equals(message)) {
                return errorType;
            }
        }
        return DEFAULT;
    }


}
