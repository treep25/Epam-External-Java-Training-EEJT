package com.epam.esm.authentication.controller;

import com.epam.esm.authentication.model.AuthenticationRefreshRequest;
import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.GoogleRequestToken;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.authentication.service.AuthenticationService;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.debug("Validation of request model for registration " + request.getUsername());

        if (DataValidation.validateRegisterRequest(request)) {
            log.debug("Return tokens for auth");
            return ResponseEntity.ok(service.register(request));
        }
        log.error("username or password cannot be empty " + request.getUsername());
        throw new ServerException("username or password cannot be empty");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        log.debug("Validation of request model authentication " + request.getUsername());

        if (DataValidation.validateAuthenticationRequest(request)) {
            log.debug("Return tokens for auth");
            return ResponseEntity.ok(service.authenticate(request));
        }
        log.error("username or password cannot be empty " + request.getUsername());
        throw new ServerException("username or password cannot be empty");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthenticationRefreshRequest token) {
        log.debug("Validation of request model for refreshing " + token.getRefreshToken());

        if (DataValidation.isStringValid(token.getRefreshToken())) {
            log.debug("Return tokens for auth");
            return ResponseEntity.ok(service.refreshToken(token));
        }
        log.error("refresh token cannot be empty or be consists of only numbers " + token.getRefreshToken());
        throw new ServerException("refresh token cannot be empty or be consists of only numbers");
    }

    @PostMapping("/google-auth")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleRequestToken token) {
        log.debug("Validation of request model for google auth " + token.getGoogleToken());

        if (DataValidation.isStringValid(token.getGoogleToken())) {
            log.debug("Return tokens for auth");
            return ResponseEntity.ok(service.authenticate(token));
        }
        log.error("google token_id cannot be empty or be consists of only numbers " + token.getGoogleToken());
        throw new ServerException("google token_id cannot be empty or be consists of only numbers");
    }

}