package com.epam.esm.authentication.controller;

import com.epam.esm.authentication.model.AuthenticationRefreshRequest;
import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.AuthenticationResponse;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.authentication.service.AuthenticationService;
import com.epam.esm.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody AuthenticationRefreshRequest token) {
        return ResponseEntity.ok(service.refreshToken(token.getRefreshToken()));
    }

}