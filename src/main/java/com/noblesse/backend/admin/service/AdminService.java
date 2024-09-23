package com.noblesse.backend.admin.service;

import com.noblesse.backend.admin.entity.Admin;
import com.noblesse.backend.admin.repository.AdminRepository;
import com.noblesse.backend.oauth2.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /** 관리자 로그인 메서드 */
    public Map<String, String> login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(admin.getAdminId());
            String refreshToken = jwtUtil.generateRefreshToken(admin.getAdminId());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("token", accessToken);
            tokens.put("refresh", refreshToken);
            return tokens;
        }
        return null;
    }

    /** 관리자 로그인 시 리프레시 토큰 재발급 메서드 */
    public Map<String, String> refreshTokens(String refreshToken) {
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            Long adminId = jwtUtil.extractUserIdFromToken(refreshToken);
            if (adminId != null) {
                Admin admin = adminRepository.findById(adminId).orElse(null);
                if (admin != null) {
                    String newAccessToken = jwtUtil.generateAccessToken(adminId);
                    String newRefreshToken = jwtUtil.generateRefreshToken(adminId);

                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("token", newAccessToken);
                    tokens.put("refresh", newRefreshToken);
                    return tokens;
                }
            }
        }
        return null;
    }
}
