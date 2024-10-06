package com.noblesse.backend.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDTO {
    private String email;
    private String password;
    private String role;
    
    // 정적 팩토리 메서드
    public static AdminDTO of(String email, String password, String role) {
        return AdminDTO.builder()
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}
