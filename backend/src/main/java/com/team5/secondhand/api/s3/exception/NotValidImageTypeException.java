package com.team5.secondhand.api.s3.exception;

public class NotValidImageTypeException extends ImageHostException {
    public NotValidImageTypeException(String message) {
        super(message);
    }
}
