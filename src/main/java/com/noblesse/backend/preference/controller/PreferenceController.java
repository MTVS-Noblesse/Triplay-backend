package com.noblesse.backend.preference.controller;

import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.service.PreferenceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preference")
public class PreferenceController {

    final private PreferenceServiceImpl preferenceServiceImpl;

    @Autowired
    public PreferenceController(PreferenceServiceImpl preferenceServiceImpl) {
        this.preferenceServiceImpl = preferenceServiceImpl;
    }

    @GetMapping
    Preference getPreference(@RequestBody Long preferenceId) {
        return preferenceServiceImpl.getPreferenceBypreferenceId(preferenceId);
    }

    @PostMapping
    public void registerPreference(@RequestBody String preferenceName) {
        preferenceServiceImpl.registerPreference(preferenceName);
    }

    @PatchMapping
    public void updatePreference(@RequestBody NewPreferenceDTO newPreferenceDTO) {
        preferenceServiceImpl.updatePreference(newPreferenceDTO);
    }

    @DeleteMapping
    public void deletePreference(@RequestParam Long preferenceId) {
        preferenceServiceImpl.deletePreference(preferenceId);
    }

}
