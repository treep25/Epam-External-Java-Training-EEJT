package com.epam.esm.jwt.google;

import com.epam.esm.jwt.openfeign.client.ApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GoogleJwtServiceTest {
    @InjectMocks
    private GoogleJwtService googleJwtService;
    @Mock
    private ApiClient apiClient;

    @Test
    void extractUsername() {
        when(apiClient.verifyGoogleToken("")).thenReturn(Map.of("email",""));

        googleJwtService.extractUsername("");
    }

    @Test
    void isTokenValid() {
        Map<String, String> propTest = Map.of("email", "", "aud", "1091690724125-tl75t5e2s6kumv8ealqksa5q6rfidcel.apps.googleusercontent.com", "iss", "https://accounts.google.com", "exp", "15162539022");
        when(apiClient.verifyGoogleToken("")).thenReturn(propTest);

        googleJwtService.isTokenValid("");
    }
}