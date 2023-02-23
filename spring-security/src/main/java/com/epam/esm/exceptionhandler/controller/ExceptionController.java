package com.epam.esm.exceptionhandler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class ExceptionController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(final HttpServletRequest request,
                                         final HttpServletResponse response) {
        log.error("Error Controller returns error message with status code");
        return new ResponseEntity<>(Map.of("HTTP Status", "" + response.getStatus(), "response body", Map.of("message", "Bad jwt structure", "link", Link.of("/api/v1/auth/refresh", () -> "refresh token"))), HttpStatus.valueOf(response.getStatus()));
    }

}
