package com.flytrap.rssreader.global.exception;

import com.flytrap.rssreader.global.model.ErrorResponse;
import javax.security.sasl.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse handleException(BindException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e.getFieldError());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleException(AuthenticationException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur("login", e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchDomainException.class)
    public ErrorResponse handleException(NoSuchDomainException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public ErrorResponse handleException(ApplicationException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ErrorResponse.occur(e);
    }
}
