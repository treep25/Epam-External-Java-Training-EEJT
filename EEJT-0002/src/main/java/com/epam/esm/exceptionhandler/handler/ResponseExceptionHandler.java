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

    @ExceptionHandler(ItemNotFoundException.class)
    protected ResponseEntity<?> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", "" + HttpStatus.NOT_FOUND, "response body", Map.of("message", ex.getMessage())),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ServerException.class)
    protected ResponseEntity<?> handleServerExceptionException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", "" + HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of("message", ex.getMessage())),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<?> handleApplicationException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                Map.of("HTTP Status", "" + HttpStatus.INTERNAL_SERVER_ERROR, "response body", Map.of("message", "Oops, something went wrong")),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
