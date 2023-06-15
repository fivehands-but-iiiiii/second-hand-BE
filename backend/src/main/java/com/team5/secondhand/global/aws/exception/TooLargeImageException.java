package com.team5.secondhand.global.aws.exception;

public class TooLargeImageException extends ImageHostingException {
    public TooLargeImageException(String message) {
        super(message);
    }
}
