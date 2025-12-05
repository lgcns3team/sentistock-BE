package com.example.SentiStock_backend.auth.repository;

import com.example.SentiStock_backend.auth.domain.RefreshToken;
import com.example.SentiStock_backend.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}
