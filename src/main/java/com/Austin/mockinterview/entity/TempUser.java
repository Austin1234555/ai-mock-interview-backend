package com.Austin.mockinterview.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "temp_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TempUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true, length = 10)
    private String mobile;

    private String password;

    private String otp;

    private LocalDateTime otpExpiry;

    private int resendCount = 0;

    private LocalDateTime lastResendTime;

    private LocalDateTime blockedUntil;   // for 24-hour block after 5 resends

    private LocalDateTime createdAt;
}