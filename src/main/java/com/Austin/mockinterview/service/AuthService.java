package com.Austin.mockinterview.service;

import com.Austin.mockinterview.dto.RegisterRequest;
import com.Austin.mockinterview.dto.RegisterResponse;
import com.Austin.mockinterview.entity.User;
import com.Austin.mockinterview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public RegisterResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new RegisterResponse("Email already registered");
        }

        // Create User object
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Later we will encrypt the password
        user.setPassword(request.getPassword());

        user.setCreatedAt(LocalDateTime.now());

        // Save user
        userRepository.save(user);

        return new RegisterResponse("User Registered Successfully");
    }
}