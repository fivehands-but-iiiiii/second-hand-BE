package com.team5.secondhand.global.aws.exception;

import com.team5.secondhand.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageHostExceptionHandler {
    @ExceptionHandler(TooLargeImageException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ErrorResponse TooLargeImageException(TooLargeImageException e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(NotValidImageTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse NotValidImageTypeException(NotValidImageTypeException e) {
        return ErrorResponse.occur(e);
    }
}