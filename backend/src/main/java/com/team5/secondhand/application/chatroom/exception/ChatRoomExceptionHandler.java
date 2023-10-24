package com.team5.secondhand.application.chatroom.exception;

import com.team5.secondhand.global.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ChatRoomExceptionHandler {
    @ExceptionHandler(ExistChatRoomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleExistChatRoomException(ExistChatRoomException e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(BuyerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExistChatRoomException(BuyerException e) {
        return ErrorResponse.occur(e);
    }
}
