package com.noblesse.backend.admin.controller;

import com.noblesse.backend.admin.dto.*;
import com.noblesse.backend.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 로그인", description = "관리자 로그인 및 토큰 갱신 API")
public class AdminController {

    private final AdminService adminService;

    /** 관리자 로그인 페이지 엔드포인트 */
    @Operation(summary = "관리자 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 정보")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequest adminLoginRequest) {
        Map<String, String> tokens = adminService.login(adminLoginRequest.getEmail(), adminLoginRequest.getPassword());
        if (tokens != null) {
            // 액세스 토큰 및 리프레시 토큰을 응답 본문에 포함
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("token", tokens.get("token"));
            jsonResponse.put("refresh", tokens.get("refresh"));
            return ResponseEntity.ok(jsonResponse);
        } else {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("code", "AUTH_003");
            errorResponse.put("message", "Invalid credentials");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /** 관리자 로그인 후 자동으로 리프레시 토큰 갱신 엔드포인트 */
    @Operation(summary = "토큰 갱신")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 리프레시 토큰"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshAdminToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            Map<String, String> tokens = adminService.refreshTokens(refreshToken);
            if (tokens != null) {
                JSONObject responseBody = new JSONObject();
                responseBody.put("token", tokens.get("token"));
                responseBody.put("refresh", tokens.get("refresh"));
                return ResponseEntity.ok(responseBody);
            }
        }
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("code", "AUTH_002");
        errorResponse.put("message", "Invalid refresh token or not an admin user");
        return ResponseEntity.status(403).body(errorResponse.toString());
    }
}