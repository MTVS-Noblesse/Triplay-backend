package com.noblesse.backend.preference.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NewPreferenceDTO {
    private Long preferenceId;
    private String newPreferenceName;

    public NewPreferenceDTO(Long preferenceId, String newPreferenceName) {
        this.preferenceId = preferenceId;
        this.newPreferenceName = newPreferenceName;
    }

    @Override
    public String toString() {
        return "NewPreferenceInfoDTO{" +
                "preferenceId=" + preferenceId +
                ", newPreferenceName='" + newPreferenceName + '\'' +
                '}';
    }
}
