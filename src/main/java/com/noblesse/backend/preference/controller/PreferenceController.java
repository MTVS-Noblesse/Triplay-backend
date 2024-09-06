package com.noblesse.backend.preference.controller;

import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.service.PreferenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PreferenceController {

    final private PreferenceRepository preferenceRepository;
    final private PreferenceServiceImpl preferenceServiceImpl;

    @Autowired
    public PreferenceController(PreferenceRepository preferenceRepository, PreferenceServiceImpl preferenceServiceImpl) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceServiceImpl = preferenceServiceImpl;
    }

    @GetMapping("/preference")
    Preference getPreference(@RequestBody Long preferenceId) {
        return preferenceRepository.findPreferenceByPreferenceId(preferenceId)
                .orElseThrow(() -> new RuntimeException("not found preference id: " + preferenceId));
    }

    @PostMapping("/preference")
    public void registerPreference(@RequestBody String preferenceName) {
        preferenceServiceImpl.registerPreference(preferenceName);
    }

    @PatchMapping("/preference")
    public void updatePreference(@RequestBody NewPreferenceDTO newPreferenceDTO) {
        preferenceServiceImpl.updatePreference(newPreferenceDTO);
    }

    @DeleteMapping("/preference")
    public void deletePreference(@RequestParam Long preferenceId) {
        preferenceServiceImpl.deletePreference(preferenceId);
    }

}
