package com.Austin.mockinterview.service;

import com.Austin.mockinterview.dto.RegisterRequest;
import com.Austin.mockinterview.dto.RegisterResponse;
import com.Austin.mockinterview.entity.User;
import com.Austin.mockinterview.repository.UserRepository;
import com.Austin.mockinterview.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Austin.mockinterview.dto.LoginRequest;
import com.Austin.mockinterview.dto.LoginResponse;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

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
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setCreatedAt(LocalDateTime.now());

        // Save user
        userRepository.save(user);

        return new RegisterResponse("User Registered Successfully");
    }
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                "Login Successful",
                user.getName(),
                user.getEmail()
        );
    }
}