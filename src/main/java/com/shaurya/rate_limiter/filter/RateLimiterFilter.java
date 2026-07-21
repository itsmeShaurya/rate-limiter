package com.shaurya.rate_limiter.filter;

import com.shaurya.rate_limiter.entity.User;
import com.shaurya.rate_limiter.exception.InvalidApiKeyException;
import com.shaurya.rate_limiter.factory.RateLimiterFactory;
import com.shaurya.rate_limiter.service.ApiKeyService;
import com.shaurya.rate_limiter.strategy.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {
    private final RateLimiterFactory factory;
    private final ApiKeyService apiKeyService;

    public RateLimiterFilter(RateLimiterFactory factory, ApiKeyService apiKeyService) {
        this.factory = factory;
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Registration endpoint should be accessible without an API key.
        if (request.getRequestURI().equals("/users") && request.getMethod().equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("X-API-Key");
        if(apiKey == null || apiKey.isBlank()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing X-API-Key header");
            return;
        }
        User user;
        try{
            user = apiKeyService.validApiKey(apiKey);
        } catch (InvalidApiKeyException ex){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ex.getMessage());
            return;
        }
        RateLimiter rateLimiter = factory.getRateLimiter();
        boolean allowed = rateLimiter.allowRequests(user.getId().toString());
        if(!allowed){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Too many requests");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
