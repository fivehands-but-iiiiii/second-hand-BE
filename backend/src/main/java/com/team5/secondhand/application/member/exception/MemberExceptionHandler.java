package com.team5.secondhand.application.member.exception;

import com.team5.secondhand.application.oauth.dto.UserProfile;
import com.team5.secondhand.global.model.ErrorResponse;
import com.team5.secondhand.global.model.ErrorResponseWithBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpSession;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@RestControllerAdvice
public class MemberExceptionHandler {

    public static final String JOIN_SESSION_KEY = "tempMember";
    private final int JOIN_SESSION_TIMEOUT = 5 * 60;
    private final HttpSession session;

    @ExceptionHandler(MemberDataCorruptedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleDataCorruptedException(MemberDataCorruptedException e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(UnauthorizedGithubMemberException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseWithBody<UserProfile> unauthorizedGithubMemberException(UnauthorizedGithubMemberException e) {
        session.setAttribute(JOIN_SESSION_KEY, e.getBody());
        session.setMaxInactiveInterval(JOIN_SESSION_TIMEOUT);
        return ErrorResponseWithBody.occur(e, e.getBody());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorizedMemberException(UnauthorizedException e) {
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(ExistMemberIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleExistMemberIdException(ExistMemberIdException e) {
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }

    @ExceptionHandler(NotValidMemberIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidMemberIdException(NotValidMemberIdException e) {
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }
}
