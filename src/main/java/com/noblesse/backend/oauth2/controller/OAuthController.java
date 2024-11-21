package com.noblesse.backend.oauth2.controller;

import com.noblesse.backend.oauth2.dto.MobileMyPageDTO;
import com.noblesse.backend.oauth2.dto.UserDTO;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import com.noblesse.backend.oauth2.security.PrincipalDetails;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import com.noblesse.backend.oauth2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserInfoById(@PathVariable("userId") Long userId) {
        try {
            UserDetails userDetails = oAuth2Service.loadUser(userId);

            if (userDetails instanceof PrincipalDetails) {
                PrincipalDetails principalDetails = (PrincipalDetails) userDetails;

                UserDTO userDTO = new UserDTO(userId, principalDetails.getUsername(), principalDetails.getEmail());
                return ResponseEntity.ok(userDTO);
            } else {
                throw new UsernameNotFoundException("User not found with id: " + userId);
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/user/mypage")
    public ResponseEntity<MobileMyPageDTO> getUserDataForMyPage(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = jwtUtil.extractUserId(authorizationHeader.substring(7));
        MobileMyPageDTO mobileMyPageDTO = oAuth2Service.getUserData(userId);
        return ResponseEntity.ok(mobileMyPageDTO);
    }
    @PatchMapping("/user/mypage")
    public ResponseEntity<MobileMyPageDTO> modifyUserDataForMyPage(@RequestHeader("Authorization") String authorizationHeader,
                                                                   @RequestBody(required = true) MobileMyPageDTO mobileMyPageDTO) {
        Long userId = jwtUtil.extractUserId(authorizationHeader.substring(7));
        oAuth2Service.modifyUser(userId, mobileMyPageDTO);
        return ResponseEntity.ok(mobileMyPageDTO);
    }
    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = jwtUtil.extractUserId(authorizationHeader.substring(7));
        oAuth2Service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
