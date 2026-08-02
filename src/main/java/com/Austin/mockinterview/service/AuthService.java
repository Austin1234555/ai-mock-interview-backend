//package com.Austin.mockinterview.service;
//
//import com.Austin.mockinterview.dto.RegisterRequest;
//import com.Austin.mockinterview.dto.RegisterResponse;
//import com.Austin.mockinterview.entity.User;
//import com.Austin.mockinterview.repository.UserRepository;
//import com.Austin.mockinterview.security.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.Austin.mockinterview.dto.LoginRequest;
//import com.Austin.mockinterview.dto.LoginResponse;
//
//import java.time.LocalDateTime;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Service
//public class AuthService {
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    public RegisterResponse register(RegisterRequest request) {
//
//        // Check if email already exists
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            return new RegisterResponse("Email already registered");
//        }
//
//        // Create User object
//        User user = new User();
//
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//
//        // Later we will encrypt the password
//        user.setPassword(
//                passwordEncoder.encode(request.getPassword())
//        );
//
//        user.setCreatedAt(LocalDateTime.now());
//
//        // Save user
//        userRepository.save(user);
//
//        return new RegisterResponse("User Registered Successfully");
//    }
//    public LoginResponse login(LoginRequest request) {
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid Password");
//        }
//
//        String token = jwtUtil.generateToken(user.getEmail());
//
//        return new LoginResponse(
//                token,
//                "Login Successful",
//                user.getName(),
//                user.getEmail()
//        );
//    }
//}





package com.Austin.mockinterview.service;

import com.Austin.mockinterview.dto.*;
import com.Austin.mockinterview.entity.TempUser;
import com.Austin.mockinterview.entity.User;
import com.Austin.mockinterview.repository.TempUserRepository;
import com.Austin.mockinterview.repository.UserRepository;
import com.Austin.mockinterview.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TempUserRepository tempUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    // ===================== REGISTER =====================
    public RegisterResponse register(RegisterRequest request) {

        // 1. Password match check
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2. Check if already registered (permanent user)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent() == false
                && userRepository.findAll().stream().anyMatch(u -> u.getMobile() != null && u.getMobile().equals(request.getMobile()))) {
            // Better way below
        }

        // Better uniqueness checks
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile number already registered");
        }

        // 3. Check if already in TempUser
        Optional<TempUser> existingTemp = tempUserRepository.findByEmail(request.getEmail());
        if (existingTemp.isPresent()) {
            TempUser temp = existingTemp.get();

            // If blocked
            if (temp.getBlockedUntil() != null && temp.getBlockedUntil().isAfter(LocalDateTime.now())) {
                throw new RuntimeException("Too many OTP requests. Try again after 24 hours.");
            }

            // Update existing temp user
            temp.setFirstName(request.getFirstName());
            temp.setLastName(request.getLastName());
            temp.setMobile(request.getMobile());
            temp.setPassword(passwordEncoder.encode(request.getPassword()));
            temp.setOtp(generateOtp());
            temp.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            temp.setLastResendTime(LocalDateTime.now());
            temp.setResendCount(0);

            tempUserRepository.save(temp);
            emailService.sendOtpEmail(temp.getEmail(), temp.getOtp());

            return new RegisterResponse("OTP sent to your email. Please verify.");
        }

        // 4. Create new TempUser
        TempUser tempUser = new TempUser();
        tempUser.setFirstName(request.getFirstName());
        tempUser.setLastName(request.getLastName());
        tempUser.setEmail(request.getEmail());
        tempUser.setMobile(request.getMobile());
        tempUser.setPassword(passwordEncoder.encode(request.getPassword()));
        tempUser.setOtp(generateOtp());
        tempUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        tempUser.setResendCount(0);
        tempUser.setLastResendTime(LocalDateTime.now());
        tempUser.setCreatedAt(LocalDateTime.now());

        tempUserRepository.save(tempUser);

        // Send OTP
        emailService.sendOtpEmail(tempUser.getEmail(), tempUser.getOtp());

        return new RegisterResponse("OTP sent to your email. Please verify.");
    }

    // ===================== VERIFY OTP =====================
    public RegisterResponse verifyOtp(VerifyOtpRequest request) {

        TempUser tempUser = tempUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No registration found for this email"));

        // Check if blocked
        if (tempUser.getBlockedUntil() != null && tempUser.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Account temporarily blocked. Try after 24 hours.");
        }

        // Check OTP expiry
        if (tempUser.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired. Please resend OTP.");
        }

        // Check OTP match
        if (!tempUser.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Move to permanent User
        User user = new User();
        user.setFirstName(tempUser.getFirstName());
        user.setLastName(tempUser.getLastName());
        user.setEmail(tempUser.getEmail());
        user.setMobile(tempUser.getMobile());
        user.setPassword(tempUser.getPassword());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        // Delete from temp
        tempUserRepository.delete(tempUser);

        return new RegisterResponse("Account verified successfully. You can now login.");
    }

    // ===================== RESEND OTP =====================
    public RegisterResponse resendOtp(ResendOtpRequest request) {

        TempUser tempUser = tempUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("No registration found for this email"));

        // Check 24-hour block
        if (tempUser.getBlockedUntil() != null && tempUser.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Too many OTP requests. Try again after 24 hours.");
        }

        // Check 30 seconds gap
        if (tempUser.getLastResendTime() != null &&
                tempUser.getLastResendTime().plusSeconds(30).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Please wait 30 seconds before resending OTP.");
        }

        // Check max 5 resends
        if (tempUser.getResendCount() >= 5) {
            tempUser.setBlockedUntil(LocalDateTime.now().plusHours(24));
            tempUserRepository.save(tempUser);
            throw new RuntimeException("Maximum resend limit reached. Try again after 24 hours.");
        }

        // Generate new OTP
        String newOtp = generateOtp();
        tempUser.setOtp(newOtp);
        tempUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        tempUser.setResendCount(tempUser.getResendCount() + 1);
        tempUser.setLastResendTime(LocalDateTime.now());

        tempUserRepository.save(tempUser);

        emailService.sendOtpEmail(tempUser.getEmail(), newOtp);

        return new RegisterResponse("OTP resent successfully.");
    }

    // ===================== LOGIN (updated) =====================
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
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail()
        );
    }

    // ===================== HELPER =====================
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}