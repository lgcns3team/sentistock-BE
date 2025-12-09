package com.example.SentiStock_backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SentiStock_backend.user.domain.UserEntity;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);
    Optional<UserEntity> findByProviderAndProviderId(String provider, String providerId);
}
