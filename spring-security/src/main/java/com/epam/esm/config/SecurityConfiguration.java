package com.epam.esm.config;

import com.epam.esm.exceptionhandler.handler.ResponseExceptionHandler;
import com.epam.esm.role.Role;
import com.epam.esm.jwt.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    //TODO to add methods
    // remake delete methods
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**","/error")
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
                .requestMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/orders", "/api/v1/orders/**", "/api/v1/users/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "api/v1/orders/**", "/api/v1/users/**").hasAuthority(Role.USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
