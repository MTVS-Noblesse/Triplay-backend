package com.noblesse.backend.preference.controller;

import com.noblesse.backend.oauth2.util.JwtUtil;
import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.service.PreferenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/preference")
public class PreferenceController {

    final private PreferenceServiceImpl preferenceServiceImpl;
    final private JwtUtil jwtUtil;

    @Autowired
    public PreferenceController(PreferenceServiceImpl preferenceServiceImpl, JwtUtil jwtUtil) {
        this.preferenceServiceImpl = preferenceServiceImpl;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<PreferenceInfo>> getUserPreferences(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        List<PreferenceInfo> preferences = preferenceServiceImpl.findSelectedUserPreferenceList(userId);
        return ResponseEntity.ok(preferences);
    }

    @PostMapping
    public ResponseEntity<String> updateUserPreferences(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody List<Long> preferenceIds) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        preferenceServiceImpl.updateUserPreferences(preferenceIds, userId);

        return ResponseEntity.ok("취향 정보 등록이 완료되었습니다.");
    }

    @DeleteMapping
    public void deletePreference(@RequestParam Long preferenceId) {
        preferenceServiceImpl.deletePreference(preferenceId);
    }

}
