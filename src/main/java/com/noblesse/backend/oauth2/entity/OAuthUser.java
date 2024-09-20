package com.noblesse.backend.oauth2.entity;

import jakarta.persistence.*;

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

    public OAuthUser() {}

    public OAuthUser(String userName, String email, String provider, String providerId, Long profileId) {
        this.userName = userName;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.profileId = profileId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public Long getId() {
        return id;
    }


    public String getUserName() {
        return userName;
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

    public String getUsername() {
        return userName;
    }

    public Long getUserId() {
        return id;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }
}
