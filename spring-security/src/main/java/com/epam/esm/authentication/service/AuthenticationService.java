package com.epam.esm.authentication.service;


import com.epam.esm.authentication.model.*;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.exceptionhandler.exception.UserInvalidData;
import com.epam.esm.jwt.google.GoogleJwtService;
import com.epam.esm.role.Role;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GoogleJwtService googleJwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();

        if (!repository.existsByName(request.getUsername())) {
            user = User.builder()
                    .name(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
        }
        Optional<User> userByName = repository.findByName(request.getUsername());
        if (userByName.isPresent()) {
            if (userByName.get().getPassword() == null) {
                user = userByName.get();
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            } else {
                throw new ServerException("Sorry, such username has already taken");
            }
        }

        repository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = repository.findByName(request.getUsername())
                .orElseThrow(() -> new AccessDeniedException("incorrect login or password"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new AccessDeniedException("incorrect login or password");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(AuthenticationRefreshRequest request) {
        User currentUser = repository.findByName(jwtService.extractUsername(request.getRefreshToken()))
                .orElseThrow(() -> new UserInvalidData("error occurred during the request"));

        if (jwtService.isTokenValid(request.getRefreshToken(), currentUser)) {

            String accessToken = jwtService.generateToken(currentUser);
            String refreshToken = jwtService.generateRefreshToken(currentUser);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new AccessDeniedException("Token is not valid");
    }

    @Transactional
    public AuthenticationResponse authenticate(GoogleRequestToken request) {
        if (googleJwtService.isTokenValid(request.getGoogleToken())) {
            String username = googleJwtService.extractUsername(request.getGoogleToken());

            if (username != null) {
                Optional<User> userByName = repository.findByName(username);
                if (userByName.isEmpty()) {
                    userByName = Optional.of(repository.save(User.builder().name(username.trim()).role(Role.USER).build()));
                }

                String accessToken = jwtService.generateToken(userByName.get());
                String refreshToken = jwtService.generateRefreshToken(userByName.get());

                return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
            }
            throw new AccessDeniedException("Bad token signature");
        }
        throw new AccessDeniedException("Token is not valid");

    }
}


