package com.m2it.orkestrator.infrastructure.adapter.out.persistence.repository;

import com.m2it.orkestrator.infrastructure.adapter.out.persistence.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenJpaRepository extends JpaRepository<VerificationTokenEntity, UUID> {

    Optional<VerificationTokenEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM VerificationTokenEntity v WHERE v.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

}
