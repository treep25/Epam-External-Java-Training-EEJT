package com.epam.esm.authentication.service;


import com.epam.esm.confirmation_token.model.ConfirmationToken;
import com.epam.esm.confirmation_token.repository.ConfirmationTokenRepository;
import com.epam.esm.confirmation_token.service.EmailService;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationService implements AuthenticationServiceInterface {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GoogleJwtService googleJwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    private final static boolean NOT_VERIFIED = false;
    private final static boolean VERIFIED = true;

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
                log.error("sorry, such username has already taken {}", request.getUsername());
                throw new ServerException("sorry, such username has already taken " + request.getUsername());
            }
        }

        return false;
    }

    private AuthenticationResponse generateResponseTokens(String accessToken, String refreshToken) {
        log.debug("Service returns authentication response with two tokens {} , {}", accessToken, refreshToken);

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

    private boolean verifyTokenConfirmation(ConfirmationToken token) {
        log.debug("Verify token and its expiration {}", token);
        if (token != null) {
            if (token.getStatus()) {
                return true;
            }
            log.error("Confirmation token has already expired {}", token);
            throw new ServerException("Confirmation token has already expired");
        }
        return false;
    }

    private void sendEmailConfirmation(User user, ConfirmationToken confirmationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getName());

        mailMessage.setSubject("Complete Registration Spring Security!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/api/v2/auth/confirm-account?token=" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);
    }

    private void generateVerificationTokenAndSend(User user) {

        ConfirmationToken confirmationToken = ConfirmationToken
                .builder()
                .confirmationToken(RandomStringUtils.randomAlphanumeric(133))
                .user(user)
                .build();

        confirmationTokenRepository.save(confirmationToken);

        log.debug("Build user model {} and confirmation token {}", user, confirmationToken);
        sendEmailConfirmation(user, confirmationToken);
    }

    private boolean verifyIfUserTryingTiRegisterButDoNotCheckEmail(RegisterRequest request) {
        return repository.existsByName(request.getUsername());
    }

    @Override
    public Map<String, ?> registerViaEmail(RegisterRequest request) {
        log.debug("Receive registration request with letter confirmation {}", request.getUsername());
        if (verifyIfUserTryingTiRegisterButDoNotCheckEmail(request)) {

            User user = repository.findByName(request.getUsername()).get();
            log.debug("verifying if user has status enabled false {}", user.getVerificationStatus());

            if (!user.getVerificationStatus()) {

                ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);

                if (!confirmationToken.getStatus()) {
                    confirmationTokenRepository.delete(confirmationToken);

                    generateVerificationTokenAndSend(user);

                    return Map.of("message", "Verify email by the link sent on your email address");
                }
                return Map.of("message", "Your verification link has already sent, verify email " + user.getName());

            } else if (verifyTokenIfHasRegisteredViaGoogle(Optional.of(user), request)) {

                user.setPassword(passwordEncoder.encode(request.getPassword()));

                log.debug("Saving user and generating access and refresh tokens");
                repository.save(user);

                return generateResponseTokens(jwtService.generateToken(user), jwtService.generateRefreshToken(user))
                        .buildRegisterMapWithTokens();
            }

            log.error("username has already existed {}", request.getUsername());
            throw new ServerException("username has already existed " + request.getUsername());
        }

        User user = User.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isEnabled(NOT_VERIFIED)
                .build();

        repository.save(user);

        generateVerificationTokenAndSend(user);

        return Map.of("message", "Verify email by the link sent on your email address");
    }
    @Override
    public Map<String, ?> confirmEmail(String confirmationToken) {
        log.debug("Receive token to confirm email {}", confirmationToken);
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (verifyTokenConfirmation(token)) {
            User byName = repository.findByName(token.getUser().getName()).get();
            byName.setEnabled(VERIFIED);

            repository.save(byName);

            confirmationTokenRepository.delete(token);

            return Map.of("message", "Email verified successfully!",
                    "tokens", generateResponseTokens(jwtService.generateToken(byName), jwtService.generateRefreshToken(byName)));
        }
        log.error("Error occurred during the request {}", token);
        throw new ServerException("Error occurred during the request");
    }
    @Override
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
                    .isEnabled(VERIFIED)
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

    @Override
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
    @Override
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

    @Override
    public AuthenticationResponse authenticate(GoogleRequestToken request) {
        log.debug("Service receives params for authentication , verifying google token id");


        if (googleJwtService.isTokenValid(request.getGoogleToken())) {
            String username = googleJwtService.extractUsername(request.getGoogleToken());

            if (username != null) {

                Optional<User> userByName = repository.findByName(username)
                        .or(() -> Optional.of(repository.save(User.builder().name(username.trim()).isEnabled(VERIFIED).role(Role.USER).build())));

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


