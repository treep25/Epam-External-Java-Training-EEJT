package com.epam.esm.authentication.service;


import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.AuthenticationResponse;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.exceptionhandler.exception.UserInvalidData;
import com.epam.esm.role.Role;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

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

    public AuthenticationResponse refreshToken(String token) {

        User currentUser = repository.findByName(jwtService.extractUsername(token))
                .orElseThrow(() -> new UserInvalidData("error occurred during the request"));

        if (jwtService.isTokenValid(token, currentUser)) {

            String accessToken = jwtService.generateToken(currentUser);
            String refreshToken = jwtService.generateRefreshToken(currentUser);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new AccessDeniedException("Token is not valid");
    }
}


