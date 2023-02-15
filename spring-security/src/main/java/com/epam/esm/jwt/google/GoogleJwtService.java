package com.epam.esm.jwt.google;

import com.epam.esm.jwt.openfeign.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class GoogleJwtService {

    private final ApiClient apiClient;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String aud;
    @Value("${google.jwt.iss}")
    private String iss;

    public GoogleJwtService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    private Map<String, String> getMapOfClaimsFromToken(String token) {
        return apiClient.verifyGoogleToken(token);
    }

    public String extractUsername(String token) {
        return getMapOfClaimsFromToken(token).get("email");
    }

    public boolean isTokenValid(String token) {

        return isTokenExpired(token) && isTokenHaveNormPayment(token);
    }

    private Long extractExpiration(String token) {
        return Long.parseLong(getMapOfClaimsFromToken(token).get("exp"));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token) + 3 * 3600 >= Instant.now().getEpochSecond();
    }

    private boolean isTokenHaveNormPayment(String token) {
        Map<String, String> mapOfClaimsFromToken = getMapOfClaimsFromToken(token);
        return mapOfClaimsFromToken.get("aud").equals(aud) && mapOfClaimsFromToken.get("iss").equals(iss);
    }
}

