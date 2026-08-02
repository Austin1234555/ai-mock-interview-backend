package com.Austin.mockinterview.repository;

import com.Austin.mockinterview.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {

    Optional<TempUser> findByEmail(String email);

    Optional<TempUser> findByMobile(String mobile);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);
}