package com.noblesse.backend.oauth2.repository;

import com.noblesse.backend.oauth2.entity.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OAuthRepository extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByProviderIdAndProvider(String providerId, String provider);
    @Modifying
    @Transactional
    @Query("UPDATE OAuthUser ou SET ou.isFired = true, ou.firedAt = CURRENT_TIMESTAMP WHERE ou.id = :id")
    void updateIsFiredAndTimestamp(Long id);



}
