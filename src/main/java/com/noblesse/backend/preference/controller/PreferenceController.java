package com.noblesse.backend.preference.controller;

import com.noblesse.backend.oauth2.util.JwtUtil;
import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.service.PreferenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<PreferenceInfo> getPreferences(@RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        return preferenceServiceImpl.findSelectedUserPreferenceList(userId);
    }

    @PostMapping
    public void updateUserPreferences(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, Object> body) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        List<Long> preferenceIds = (List<Long>) body.get("preferenceIds");
        preferenceServiceImpl.updateUserPreferences(preferenceIds, userId);
    }

    @DeleteMapping
    public void deletePreference(@RequestParam Long preferenceId) {
        preferenceServiceImpl.deletePreference(preferenceId);
    }

}
