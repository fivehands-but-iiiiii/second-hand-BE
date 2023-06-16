package com.team5.secondhand.global.exception;

import com.team5.secondhand.api.member.exception.UnauthorizedException;
import com.team5.secondhand.global.aws.exception.ImageHostingException;
import com.team5.secondhand.global.aws.exception.TooLargeImageException;
import com.team5.secondhand.global.dto.ErrorResponse;
import com.team5.secondhand.global.dto.ErrorResponseWithBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ErrorResponse handleTooLargeImageException(TooLargeImageException e) {
        return ErrorResponse.occur(e);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleImageUploadException(ImageHostingException e) {
        return ErrorResponse.occur(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllException(Exception e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseWithBody handleUnauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage());
        return ErrorResponseWithBody.occur(e, e.getBody());
    }
}
