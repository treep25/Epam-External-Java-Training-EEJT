package com.epam.esm.authentication.model;

import com.epam.esm.jacoco.JacocoCoverageExcludesGenerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JacocoCoverageExcludesGenerated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;

    public Map<String,String> buildRegisterMapWithTokens(){
        return Map.of("accessToken",accessToken,"refreshToken",refreshToken);
    }
}
