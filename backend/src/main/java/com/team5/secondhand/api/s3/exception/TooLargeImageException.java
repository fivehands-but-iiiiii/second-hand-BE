package com.team5.secondhand.api.s3.exception;

public class TooLargeImageException extends ImageHostException {
    public TooLargeImageException(String message) {
        super(message);
    }
}
