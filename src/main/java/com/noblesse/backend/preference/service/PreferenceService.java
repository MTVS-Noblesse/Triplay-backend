package com.noblesse.backend.preference.service;

import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;

import java.util.List;

public interface PreferenceService {

    Preference getPreferenceBypreferenceId(Long preferenceId);
    void registerPreference(String preferenceName);
    void updatePreference(NewPreferenceDTO preferenceInfoDTO);
    void updateUserPreferences(List<Long> preferenceIds, Long userId);
    void deletePreference(Long id);
    List<PreferenceInfo> findSelectedUserPreferenceList(Long userId);
}
