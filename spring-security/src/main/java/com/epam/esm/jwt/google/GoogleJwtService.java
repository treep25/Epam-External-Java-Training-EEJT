package com.epam.esm.jwt.google;

import com.epam.esm.jwt.openfeign.client.ApiClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class GoogleJwtService {

    private final ApiClient apiClient;

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
        return mapOfClaimsFromToken.get("aud").equals("1091690724125-tl75t5e2s6kumv8ealqksa5q6rfidcel.apps.googleusercontent.com") && mapOfClaimsFromToken.get("iss").equals("https://accounts.google.com");
    }
}

