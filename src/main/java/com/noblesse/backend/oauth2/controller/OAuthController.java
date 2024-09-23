package com.noblesse.backend.oauth2.controller;

import com.noblesse.backend.oauth2.dto.UserDTO;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import com.noblesse.backend.oauth2.security.PrincipalDetails;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import com.noblesse.backend.oauth2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Controller
public class OAuthController {
    @Autowired
    private OAuthRepository oAuthRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private OAuth2Service oAuth2Service;

    @GetMapping("/refresh")
    public ResponseEntity<?> checkRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization").substring(7);
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            Long userId = jwtUtil.extractUserId(refreshToken);
            String jwtToken = jwtUtil.generateAccessToken(userId);

            // JSON 객체 생성
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("token", jwtToken);

            return ResponseEntity.ok(jsonResponse.toString());
        } else {
            // JSON 객체 생성
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("code", "AUTH_001");
            errorResponse.put("message", "Invalid JWT token");

            return ResponseEntity.status(401).body(errorResponse.toString());
        }
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.substring(7);
        if (jwtUtil.validateAccessToken(accessToken)) {
            Long userId = jwtUtil.extractUserId(accessToken);
            String userName = jwtUtil.extractUserName(accessToken);

            UserDTO userDTO = new UserDTO(userId, userName);
            System.out.println("*******" + userDTO);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserInfoById(@PathVariable("userId") Long userId) {
        try {
            UserDetails userDetails = oAuth2Service.loadUser(userId);

            UserDTO userDTO = new UserDTO(userId, userDetails.getUsername());
            return ResponseEntity.ok(userDTO);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
