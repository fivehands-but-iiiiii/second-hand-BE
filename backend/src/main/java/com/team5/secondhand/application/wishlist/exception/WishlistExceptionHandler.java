package com.team5.secondhand.application.wishlist.exception;

import com.team5.secondhand.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WishlistExceptionHandler {

    @ExceptionHandler(ExistWishlistException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleExistWishlistException(ExistWishlistException e) {
        return ErrorResponse.occur(e);
    }
}
