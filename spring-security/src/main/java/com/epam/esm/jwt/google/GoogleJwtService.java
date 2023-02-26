package com.epam.esm.jwt.google;

import com.epam.esm.jwt.openfeign.client.ApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
@Slf4j
@Service
public class GoogleJwtService {

    private final ApiClient apiClient;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String aud;
    @Value("${google.jwt.iss}")
    private String iss;
    private static final long THREE_HOURS = 3 * 3600;
    private static final String EXPIRATIONS_DATE = "exp";
    private static final String EMAIL = "email";
    private static final String AUDIENCE = "aud";
    private static final String ISSUER = "iss";

    public GoogleJwtService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    private Map<String, String> getMapOfClaimsFromToken(String token) {
        log.debug("Getting claims from token");
        return apiClient.verifyGoogleToken(token);
    }

    public String extractUsername(String token) {
        log.debug("Getting name from token");
        return getMapOfClaimsFromToken(token).get(EMAIL);
    }

    public boolean isTokenValid(String token) {
        log.debug("Validation of token");
        return isTokenExpired(token) && isTokenHaveNormPayment(token);
    }

    private Long extractExpiration(String token) {
        log.debug("Getting expiration");
        return Long.parseLong(getMapOfClaimsFromToken(token).get(EXPIRATIONS_DATE));
    }

    private boolean isTokenExpired(String token) {
        log.debug("Is token expired");
        return extractExpiration(token) + THREE_HOURS >= Instant.now().getEpochSecond();
    }

    private boolean isTokenHaveNormPayment(String token) {
        log.debug("Validation token payment");
        Map<String, String> mapOfClaimsFromToken = getMapOfClaimsFromToken(token);
        return mapOfClaimsFromToken.get(AUDIENCE).equals(aud) && mapOfClaimsFromToken.get(ISSUER).equals(iss);
    }
}

