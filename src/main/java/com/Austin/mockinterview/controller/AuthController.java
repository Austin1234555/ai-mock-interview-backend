//package com.Austin.mockinterview.controller;
//
//import com.Austin.mockinterview.dto.RegisterRequest;
//import com.Austin.mockinterview.dto.RegisterResponse;
//import com.Austin.mockinterview.service.AuthService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import com.Austin.mockinterview.dto.LoginRequest;
//import com.Austin.mockinterview.dto.LoginResponse;
//import org.springframework.http.ResponseEntity;
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/register")
//    public RegisterResponse register(
//            @Valid @RequestBody RegisterRequest request) {
//
//        return authService.register(request);
//
//    }
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(
//            @Valid @RequestBody LoginRequest request) {
//
//        LoginResponse response = authService.login(request);
//
//        return ResponseEntity.ok(response);
//    }
//
//}




package com.Austin.mockinterview.controller;

import com.Austin.mockinterview.dto.*;
import com.Austin.mockinterview.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register → creates TempUser + sends OTP
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    // Verify OTP → moves to permanent User
    @PostMapping("/verify-otp")
    public ResponseEntity<RegisterResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    // Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<RegisterResponse> resendOtp(
            @Valid @RequestBody ResendOtpRequest request) {

        return ResponseEntity.ok(authService.resendOtp(request));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}