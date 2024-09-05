package com.noblesse.backend.preference.service;

import com.noblesse.backend.preference.dto.NewPreferenceDTO;

import java.util.List;

public interface PreferenceService {

    void registPreference(String preferenceName);
    void updatePreference(NewPreferenceDTO preferenceInfoDTO);
    void updateUserPreferences(List<Long> preferenceIds, Long userId);
}
