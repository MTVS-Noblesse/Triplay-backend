package com.noblesse.backend.manage.user.repository;

import com.noblesse.backend.oauth2.entity.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<OAuthUser, Long> {
}
