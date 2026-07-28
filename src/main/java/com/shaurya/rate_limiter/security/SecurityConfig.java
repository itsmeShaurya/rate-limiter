package com.shaurya.rate_limiter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;

    public SecurityConfig(ApiKeyAuthenticationFilter apiKeyAuthenticationFilter) {
        this.apiKeyAuthenticationFilter = apiKeyAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //Disable CSRF protection (stateless api)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize

                        // Allow Spring Boot's internal error endpoint.
                        .requestMatchers("/error").permitAll()

                        // Allow new users to register without authentication.
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // Every other endpoint requires authentication.
                        .anyRequest().authenticated()
                )

                // Add our custom API Key filter before UsernamePasswordAuthenticationFilter.
                .addFilterBefore(
                        apiKeyAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                // Disable default HTTP Basic Authentication.
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
