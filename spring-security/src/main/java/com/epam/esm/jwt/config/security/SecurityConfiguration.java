package com.epam.esm.jwt.config.security;

import com.epam.esm.jwt.filter.JwtAuthenticationFilter;
import com.epam.esm.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final AuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors().disable()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**", "/error")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/certificates",
                        "/api/v1/certificates/**",
                        "/api/v1/certificates/search/**",
                        "/api/v1/tags",
                        "/api/v1/tags/**",
                        "/api/v1/tags/widely-used")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/certificates", "/api/v1/tags", "api/v1/orders/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PATCH, "/api/v1/certificates/**", "/api/v1/certificates/update-price/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/certificates/**", "/api/v1/tags/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/orders", "/api/v1/users").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAuthority(Role.USER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**", "/api/v1/orders/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);
        return http.build();
    }


}
