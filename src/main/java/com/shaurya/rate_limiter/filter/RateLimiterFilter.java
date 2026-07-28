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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {
    private final RateLimiterFactory factory;

    public RateLimiterFilter(RateLimiterFactory factory, ApiKeyService apiKeyService) {
        this.factory = factory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Skip rate limiting for user registration because no authenticated user exists yet.
        if (request.getRequestURI().equals("/users") && request.getMethod().equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Retrieve the authenticated user from Spring Security.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        RateLimiter rateLimiter = factory.getRateLimiter();
        boolean allowed = rateLimiter.allowRequests(user.getId().toString());
        if(!allowed){
            response.sendError(429, "Too many requests");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
