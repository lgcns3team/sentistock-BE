package com.example.SentiStock_backend.auth.repository;

import com.example.SentiStock_backend.auth.domain.entity.RefreshToken;
import com.example.SentiStock_backend.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}
