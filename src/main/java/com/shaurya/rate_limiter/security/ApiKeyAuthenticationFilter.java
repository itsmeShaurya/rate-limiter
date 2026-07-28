package com.shaurya.rate_limiter.security;

import com.shaurya.rate_limiter.entity.User;
import com.shaurya.rate_limiter.exception.InvalidApiKeyException;
import com.shaurya.rate_limiter.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private final ApiKeyService apiKeyService;

    public ApiKeyAuthenticationFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Allow anonymous access to the user registration endpoint.
        if (request.getRequestURI().equals("/users") && request.getMethod().equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Read the API Key from the request header.
        String apiKey = request.getHeader("X-API-Key");

        // Reject the request if the API Key is missing or blank.
        if(apiKey == null || apiKey.isBlank()){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing X-API-Key header");
            return;
        }

        User user;
        try{
            // Validate the API Key and retrieve the corresponding user.
            user = apiKeyService.validApiKey(apiKey);
        } catch (InvalidApiKeyException ex){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }

        // Create an Authentication object representing the authenticated user.
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        // Store the Authentication object in Spring Security's SecurityContext.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
