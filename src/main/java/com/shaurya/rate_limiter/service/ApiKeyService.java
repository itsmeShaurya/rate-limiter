package com.shaurya.rate_limiter.service;

import com.shaurya.rate_limiter.entity.User;
import com.shaurya.rate_limiter.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {
    private final UserRepository userRepository;

    public ApiKeyService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user and generates a unique API Key.
     */
    public User createUser(String username){
        // Check if the username already exists.
        if(userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setApiKey(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    /**
     * Finds a user using the API Key.
     */
    public Optional<User> getUserByApiKey(String apikey){
        return userRepository.findByApiKey(apikey);
    }

    public User validApiKey(String apiKey){
        return userRepository.findByApiKey(apiKey).orElseThrow(() -> new RuntimeException("Invalid API Key"));
    }
}
