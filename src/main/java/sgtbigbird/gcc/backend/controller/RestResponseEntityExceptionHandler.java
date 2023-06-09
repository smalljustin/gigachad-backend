package sgtbigbird.gcc.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { AuthenticationException.class })
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        String bodyOfResponse = "Unauthorized UUID!";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value
            = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleBadUUIDException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Couldn't parse your UUID!";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
}
