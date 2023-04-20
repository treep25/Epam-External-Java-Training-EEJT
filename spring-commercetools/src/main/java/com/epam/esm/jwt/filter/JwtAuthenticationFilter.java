package com.epam.esm.jwt.filter;

import com.epam.esm.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_HEADER = "Bearer ";
    private final Object credentials = null;
    private static final int LENGTH_OF_HEADER_BEARER = BEARER_HEADER.length();

    private void authenticateToken(UserDetails userDetails,HttpServletRequest request){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                credentials,
                userDetails.getAuthorities()
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        final String jwt;
        final String username;
        try {
            if (authHeader == null || !authHeader.startsWith(BEARER_HEADER)) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(LENGTH_OF_HEADER_BEARER);
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    authenticateToken(userDetails,request);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            response.setHeader("error", ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
