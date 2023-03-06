package com.epam.esm.authentication.service;

import com.epam.esm.authentication.model.*;

import java.util.Map;

public interface AuthenticationServiceInterface {

    Map<String, ?> registerViaEmail(RegisterRequest request);
    Map<String, ?> confirmEmail(String confirmationToken);
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(AuthenticationRefreshRequest request);
    AuthenticationResponse authenticate(GoogleRequestToken request);
}
