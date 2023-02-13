package com.epam.esm.jwt.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class GoogleJwtService {

    private Map<String, String> getMapOfClaimsFromToken(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

//        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        try {
            return new ObjectMapper().readValue(payload, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    public String extractUsername(String token) {
        return getMapOfClaimsFromToken(token).get("email");
    }

    //TODO more difficult validation ver signature
    public boolean isTokenValid(String token) {
        return isTokenExpired(token);
    }

    private Long extractExpiration(String token) {
        return Long.parseLong(getMapOfClaimsFromToken(token).get("exp"));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token) + 3 * 3600 >= Instant.now().getEpochSecond();
    }
}

