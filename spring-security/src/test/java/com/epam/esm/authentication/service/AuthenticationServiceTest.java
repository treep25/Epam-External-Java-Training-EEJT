package com.epam.esm.authentication.service;

import com.epam.esm.authentication.model.AuthenticationRefreshRequest;
import com.epam.esm.authentication.model.AuthenticationRequest;
import com.epam.esm.authentication.model.GoogleRequestToken;
import com.epam.esm.authentication.model.RegisterRequest;
import com.epam.esm.exceptionhandler.exception.ServerException;
import com.epam.esm.exceptionhandler.exception.UserInvalidData;
import com.epam.esm.jwt.google.GoogleJwtService;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.role.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationServiceMock;

    @Mock
    private UserRepository repositoryMock;
    @Mock
    private JwtService jwtServiceMock;
    @Mock
    private GoogleJwtService googleJwtServiceMock;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void registerTest_ReturnTokens_WhenOk() {
        User user = User.builder().name("123123").password("").role(Role.USER).build();
        RegisterRequest request = RegisterRequest.builder().username("123123").password("wqeqweq").build();
        when(repositoryMock.existsByName(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("");
        when(repositoryMock.findByName(request.getUsername())).thenReturn(Optional.empty());

        when(repositoryMock.save(user)).thenReturn(user);

        authenticationServiceMock.register(request);
    }

    @Test
    void registerTest_ReturnTokens_WhenUserHasRegisteredViaGoogle_WithoutToken() {
        User user = User.builder().name("123123").password("").role(Role.USER).build();
        RegisterRequest request = RegisterRequest.builder().username("123123").password("wqeqweq").build();
        when(repositoryMock.existsByName(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("");
        when(repositoryMock.findByName(request.getUsername())).thenReturn(Optional.of(User.builder().name("123123").password(null).role(Role.USER).build()));

        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () ->authenticationServiceMock.register(request));

        assertEquals("bad token signature or token is empty", thrown.getMessage());
    }

    @Test
    void registerTest_ReturnTokens_WhenUserHasRegisteredViaGoogle_WithTokenCorrect() {
        User user = User.builder().name("123123").password("").role(Role.USER).build();
        RegisterRequest request = RegisterRequest.builder().username("123123").password("wqeqweq").googleToken("").build();
        when(repositoryMock.existsByName(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("");
        when(repositoryMock.findByName(request.getUsername())).thenReturn(Optional.of(User.builder().name("123123").password(null).role(Role.USER).build()));
        when(googleJwtServiceMock.isTokenValid("")).thenReturn(true);
        when(googleJwtServiceMock.extractUsername("")).thenReturn("123123");
        when(repositoryMock.save(user)).thenReturn(user);

        authenticationServiceMock.register(request);
    }

    @Test
    void registerTest_ReturnTokens_WhenUserRegisterHasAlreadyExistedUsername() {
        RegisterRequest request = RegisterRequest.builder().username("123123").password("wqeqweq").build();
        when(repositoryMock.existsByName(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("");
        when(repositoryMock.findByName(request.getUsername())).thenReturn(Optional.of(User.builder().name("123123").password("sdf").role(Role.USER).build()));

        ServerException thrown = assertThrows(ServerException.class,
                () -> authenticationServiceMock.register(request));

        assertEquals("sorry, such username has already taken "+ request.getUsername(), thrown.getMessage());
    }

    @Test
    void authenticate_ReturnTokens_ForAll() {
        AuthenticationRequest request = AuthenticationRequest.builder().username("Name").password("password").build();
        when(repositoryMock.findByName(request.getUsername())).thenReturn(Optional.ofNullable(User.builder().name("name").id(1L).role(Role.USER).password("").build()));
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ))).thenReturn(null);

        when(jwtServiceMock.generateToken(User.builder().name("name").id(1L).role(Role.USER).password("").build())).thenReturn("");
        when(jwtServiceMock.generateRefreshToken(User.builder().name("name").id(1L).role(Role.USER).password("").build())).thenReturn("");

        authenticationServiceMock.authenticate(request);
    }

    @Test
    void authenticate_ReturnAccessDenied_ForAll_WhenIncorrectLoginOrPassword() {
        AuthenticationRequest request = AuthenticationRequest.builder().username("Name").password("password").build();
        when(repositoryMock.findByName(request.getUsername())).thenReturn(Optional.empty());

        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () -> authenticationServiceMock.authenticate(request));

        assertEquals("incorrect login or password", thrown.getMessage());
    }

    @Test
    void refreshTokenTest_ReturnTokens_ForAll() {
        AuthenticationRefreshRequest request = AuthenticationRefreshRequest.builder().refreshToken("").build();
        User build = User.builder().name("").role(Role.USER).password("").build();

        when(jwtServiceMock.extractUsername(request.getRefreshToken())).thenReturn("");
        when(repositoryMock.findByName("")).thenReturn(Optional.of(build));
        when(jwtServiceMock.isTokenValid("",build)).thenReturn(true);
        when(jwtServiceMock.generateRefreshToken(build)).thenReturn("");
        when(jwtServiceMock.generateToken(build)).thenReturn("");

        authenticationServiceMock.refreshToken(request);
    }

    @Test
    void refreshTokenTest_ReturnAccessDenied_ForAll_WhenIncorrectToken() {
        AuthenticationRefreshRequest request = AuthenticationRefreshRequest.builder().refreshToken("").build();
        User build = User.builder().name("").role(Role.USER).password("").build();
        when(jwtServiceMock.extractUsername(request.getRefreshToken())).thenReturn("");

        when(repositoryMock.findByName("")).thenReturn(Optional.of(build));
        when(jwtServiceMock.isTokenValid("",build)).thenReturn(false);

        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () -> authenticationServiceMock.refreshToken(request));

        assertEquals("token is not valid", thrown.getMessage());
    }

    @Test
    void refreshTokenTest_ReturnUserInvalidData_ForAll_WhenIncorrectToken() {
        AuthenticationRefreshRequest request = AuthenticationRefreshRequest.builder().refreshToken("").build();

        when(jwtServiceMock.extractUsername(request.getRefreshToken())).thenReturn("");
        when(repositoryMock.findByName("")).thenReturn(Optional.empty());

        UserInvalidData thrown = assertThrows(UserInvalidData.class,
                () -> authenticationServiceMock.refreshToken(request));

        assertEquals("error occurred during the request", thrown.getMessage());

    }

    @Test
    void authenticateGoogle_ReturnTokens_ForAll() {
        GoogleRequestToken googleRequestToken = GoogleRequestToken.builder().googleToken("").build();
        User build = User.builder().name("").role(Role.USER).build();

        when(googleJwtServiceMock.isTokenValid(googleRequestToken.getGoogleToken())).thenReturn(true);
        when(googleJwtServiceMock.extractUsername(googleRequestToken.getGoogleToken())).thenReturn("");
        when(repositoryMock.findByName("")).thenReturn(Optional.of(build));
        when(jwtServiceMock.generateToken(build)).thenReturn("");
        when(jwtServiceMock.generateRefreshToken(build)).thenReturn("");

        authenticationServiceMock.authenticate(googleRequestToken);
    }

    @Test
    void authenticateGoogle_ReturnTAccessDenied_ForAll_WhenBadToken() {
        GoogleRequestToken googleRequestToken = GoogleRequestToken.builder().googleToken("").build();
        User build = User.builder().name("").role(Role.USER).build();

        when(googleJwtServiceMock.isTokenValid(googleRequestToken.getGoogleToken())).thenReturn(false);

        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () -> authenticationServiceMock.authenticate(googleRequestToken));

        assertEquals("token is not valid", thrown.getMessage());
    }

    @Test
    void authenticateGoogle_ReturnTAccessDenied_ForAll_WhenTokenIsNotValid() {
        GoogleRequestToken googleRequestToken = GoogleRequestToken.builder().googleToken("").build();

        when(googleJwtServiceMock.isTokenValid(googleRequestToken.getGoogleToken())).thenReturn(true);
        when(googleJwtServiceMock.extractUsername(googleRequestToken.getGoogleToken())).thenReturn(null);

        AccessDeniedException thrown = assertThrows(AccessDeniedException.class,
                () -> authenticationServiceMock.authenticate(googleRequestToken));

        assertEquals("bad token signature", thrown.getMessage());
    }

}