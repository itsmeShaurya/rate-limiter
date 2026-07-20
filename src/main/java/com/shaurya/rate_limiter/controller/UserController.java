package com.shaurya.rate_limiter.controller;

import com.shaurya.rate_limiter.entity.User;
import com.shaurya.rate_limiter.service.ApiKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final ApiKeyService apiKeyService;

    public UserController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestParam String username){
        User user = apiKeyService.createUser(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
