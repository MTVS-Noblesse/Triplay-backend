package com.noblesse.backend.oauth2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user")
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String email;
    private String provider;
    private String providerId;
    private Long profileId;

    // 추가
    private LocalDateTime firedAt; // 정지 일자
    private boolean isFired; // 정지 유무

    public OAuthUser() {}

    public OAuthUser(String userName, String email, String provider, String providerId, Long profileId) {
        this.userName = userName;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.profileId = profileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Long getUserId() {
        return id;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public LocalDateTime getFiredAt() {
        return firedAt;
    }

    public void setFiredAt(LocalDateTime firedAt) {
        this.firedAt = firedAt;
    }

    public boolean isFired() {
        return isFired;
    }

    public void setFired(boolean fired) {
        isFired = fired;
    }
}
