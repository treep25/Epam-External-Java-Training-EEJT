package com.epam.esm.authentication.service;


import com.epam.esm.authentication.model.*;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.exceptionhandler.exception.UserInvalidData;
import com.epam.esm.jwt.google.GoogleJwtService;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GoogleJwtService googleJwtService;
    private final AuthenticationManager authenticationManager;

    private boolean verifyTokenIfHasRegisteredViaGoogle(Optional<User> currentUserByName, RegisterRequest request) {

        if (currentUserByName.isPresent()) {
            if (currentUserByName.get().getPassword() == null) {
                if (request.getGoogleToken() != null
                        && googleJwtService.isTokenValid(request.getGoogleToken())
                        && googleJwtService.extractUsername(request.getGoogleToken()).equals(currentUserByName.get().getName())) {

                    return true;
                } else {
                    throw new AccessDeniedException("bad token signature or token is empty");
                }
            } else {
                log.error("sorry, such username has already taken {} , Transaction has been ended ROLLBACK", request.getUsername());
                throw new ServerException("sorry, such username has already taken " + request.getUsername());
            }
        }

        return false;
    }

    private AuthenticationResponse generateResponseTokens(String accessToken, String refreshToken) {
        log.debug("Service returns authentication response with two tokens, transaction has been ended success ");
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void managerAuthentication(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            log.error("incorrect login or password");
            throw new AccessDeniedException("incorrect login or password");
        }
    }

    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Transaction has been started");
        log.debug("Service receives params for registration {}", request.getUsername());

        User user = new User();

        log.debug("Verifying existing such username {}", request.getUsername());
        if (!repository.existsByName(request.getUsername())) {
            user = User.builder()
                    .name(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
        }
        log.debug("Verifying existing od such username registered via google");

        Optional<User> currentUserByName = repository.findByName(request.getUsername());

        if (verifyTokenIfHasRegisteredViaGoogle(currentUserByName, request)) {
            user = currentUserByName.get();
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        log.debug("Saving user and generating access and refresh tokens");
        repository.save(user);

        return generateResponseTokens(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Service receives params for authentication {} , verifying login and password", request.getUsername());


        User user = repository.findByName(request.getUsername())
                .orElseThrow(() -> {
                    log.error("incorrect login or password");
                    return new AccessDeniedException("incorrect login or password");
                });

        managerAuthentication(request);

        log.debug("Service returns authentication response with two tokens");

        return generateResponseTokens(jwtService.generateToken(user), jwtService.generateRefreshToken(user));
    }

    public AuthenticationResponse refreshToken(AuthenticationRefreshRequest request) {
        log.debug("Service receives params for refreshing {} , verifying refresh token", request.getRefreshToken());

        User currentUser = repository.findByName(jwtService.extractUsername(request.getRefreshToken()))
                .orElseThrow(() -> {
                    log.error("error occurred during the request");
                    return new UserInvalidData("error occurred during the request");
                });

        log.debug("Validation of refresh token");
        if (jwtService.isTokenValid(request.getRefreshToken(), currentUser)) {

            log.debug("Service returns authentication response with two tokens");

            return generateResponseTokens(jwtService.generateToken(currentUser), jwtService.generateRefreshToken(currentUser));

        }
        log.error("token is not valid {}", request.getRefreshToken());
        throw new AccessDeniedException("token is not valid");
    }


    public AuthenticationResponse authenticate(GoogleRequestToken request) {
        log.debug("Service receives params for authentication , verifying google token id");


        if (googleJwtService.isTokenValid(request.getGoogleToken())) {
            String username = googleJwtService.extractUsername(request.getGoogleToken());

            if (username != null) {

                Optional<User> userByName = repository.findByName(username)
                        .or(() -> Optional.of(repository.save(User.builder().name(username.trim()).role(Role.USER).build())));

                log.debug("Service returns authentication response with two tokens");

                return generateResponseTokens(jwtService.generateToken(userByName.get()),
                        jwtService.generateRefreshToken(userByName.get()));

            }
            log.error("bad token signature");
            throw new AccessDeniedException("bad token signature");
        }
        log.error("token is not valid");
        throw new AccessDeniedException("token is not valid");

    }
}


