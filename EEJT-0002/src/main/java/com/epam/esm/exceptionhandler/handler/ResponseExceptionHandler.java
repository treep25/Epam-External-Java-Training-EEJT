package com.epam.esm.exceptionhandler.handler;

import com.epam.esm.exceptionhandler.exception.ApplicationException;
import com.epam.esm.exceptionhandler.exception.ItemNotFoundException;
import com.epam.esm.exceptionhandler.exception.ServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLSyntaxErrorException;
import java.util.Map;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String RESPONSE_BODY = "response body";
    private static final String HTTP_STATUS = "HTTP Status";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_FOR_APPLICATION_EXCEPTION = "Oops, something went wrong";

    @ExceptionHandler(ItemNotFoundException.class)
    protected ResponseEntity<?> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of(HTTP_STATUS, "" + HttpStatus.NOT_FOUND, RESPONSE_BODY, Map.of(MESSAGE, ex.getMessage())),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ServerException.class)
    protected ResponseEntity<?> handleServerExceptionException(ServerException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of(HTTP_STATUS, "" + HttpStatus.INTERNAL_SERVER_ERROR, RESPONSE_BODY, Map.of(MESSAGE, ex.getMessage())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<?> handleApplicationException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of(HTTP_STATUS, "" + HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of(MESSAGE, MESSAGE_FOR_APPLICATION_EXCEPTION)),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
