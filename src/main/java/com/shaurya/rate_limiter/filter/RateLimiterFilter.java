package com.shaurya.rate_limiter.filter;

import com.shaurya.rate_limiter.factory.RateLimiterFactory;
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

    public RateLimiterFilter(RateLimiterFactory factory) {
        this.factory = factory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        if(userId == null || userId.isBlank()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing X-USER-ID header");
            return;
        }
        RateLimiter rateLimiter = factory.getRateLimiter();
        boolean allowed = rateLimiter.allowRequests(userId);
        if(!allowed){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Too many requests");
            return;
        }
        filterChain.doFilter(request,response);
    }
}
