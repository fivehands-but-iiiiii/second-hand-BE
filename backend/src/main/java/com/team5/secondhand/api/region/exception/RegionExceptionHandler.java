package com.team5.secondhand.api.region.exception;

import com.team5.secondhand.global.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RegionExceptionHandler {
    @ExceptionHandler(EmptyBasedRegionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEmptyBasedRegionException(EmptyBasedRegionException e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(NoMainRegionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoMainRegionException(NoMainRegionException e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(NotValidRegionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidRegionException(NotValidRegionException e) {
        return ErrorResponse.occur(e);
    }
}
