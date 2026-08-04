package com.shaurya.rate_limiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitTestController {

    @GetMapping("/api/test")
    public String test(){
        return "Request successful";
    }
}
