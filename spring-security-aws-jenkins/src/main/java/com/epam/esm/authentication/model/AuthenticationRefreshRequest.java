package com.epam.esm.authentication.model;

import com.epam.esm.jacoco.JacocoCoverageExcludesGenerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacocoCoverageExcludesGenerated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRefreshRequest {
    private String refreshToken;
}
