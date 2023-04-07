package com.epam.esm.exceptionhandler.handler;


import com.epam.esm.exceptionhandler.exception.ApplicationException;
import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.exceptionhandler.exception.UserInvalidData;
import feign.FeignException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String RESPONSE_BODY = "response body";
    private static final String HTTP_STATUS = "HTTP Status";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_FOR_APPLICATION_EXCEPTION = "Oops, something went wrong";

    private Map<String, ?> buildResponseMap(HttpStatus status, String message) {
        return Map.of(HTTP_STATUS, "" + status, RESPONSE_BODY, Map.of(MESSAGE, message));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    protected ResponseEntity<?> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {

        return handleExceptionInternal(ex,
                buildResponseMap(HttpStatus.NOT_FOUND, ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ServerException.class)
    protected ResponseEntity<?> handleServerExceptionException(ServerException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                buildResponseMap(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<?> handleApplicationException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                buildResponseMap(HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE_FOR_APPLICATION_EXCEPTION),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({AccessDeniedException.class, UserInvalidData.class})
    protected ResponseEntity<?> handleUserInvalidDataException(AccessDeniedException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                buildResponseMap(HttpStatus.FORBIDDEN, ex.getMessage()),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(Exception ex) {
        return new ResponseEntity<>(buildResponseMap(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<?> handleAuthenticationException(FeignException ex) {
        return new ResponseEntity<>(buildResponseMap(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid Value - verify your jwt token"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<?> handleJwtException(JwtException ex) {
        return new ResponseEntity<>(buildResponseMap(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
