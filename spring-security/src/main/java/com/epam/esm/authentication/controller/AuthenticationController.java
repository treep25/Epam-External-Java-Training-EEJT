package com.epam.esm.authentication.controller;

import com.epam.esm.authentication.model.AuthenticationRefreshRequest;
import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.GoogleRequestToken;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.authentication.service.AuthenticationService;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.utils.validation.DataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (DataValidation.validateRegisterRequest(request)) {

            return ResponseEntity.ok(service.register(request));
        }
        throw new ServerException("username or password cannot be empty");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        if (DataValidation.validateAuthenticationRequest(request)) {

            return ResponseEntity.ok(service.authenticate(request));
        }
        throw new ServerException("username or password cannot be empty");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AuthenticationRefreshRequest token) {
        if (DataValidation.isStringValid(token.getRefreshToken())) {

            return ResponseEntity.ok(service.refreshToken(token));
        }
        throw new ServerException("refresh token cannot be empty or be consists of only numbers");
    }

    @PostMapping("/google-auth")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleRequestToken token) {
        if (DataValidation.isStringValid(token.getGoogleToken())) {

            return ResponseEntity.ok(service.authenticate(token));
        }
        throw new ServerException("google token_id cannot be empty or be consists of only numbers");
    }

}