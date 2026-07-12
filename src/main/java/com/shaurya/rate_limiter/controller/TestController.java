package com.shaurya.rate_limiter.controller;

import com.shaurya.rate_limiter.service.RateLimiterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(String userId){
        return "Hello";
    }
}
