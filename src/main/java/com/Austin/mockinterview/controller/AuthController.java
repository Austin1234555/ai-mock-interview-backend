package com.Austin.mockinterview.controller;

import com.Austin.mockinterview.dto.RegisterRequest;
import com.Austin.mockinterview.dto.RegisterResponse;
import com.Austin.mockinterview.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.Austin.mockinterview.dto.LoginRequest;
import com.Austin.mockinterview.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authService.register(request);

    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

}