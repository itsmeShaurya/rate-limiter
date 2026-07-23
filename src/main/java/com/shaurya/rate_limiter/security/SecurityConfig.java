package com.shaurya.rate_limiter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
                // Enable HTTP Basic Authentication.
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
