package com.noblesse.backend.preference.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Embeddable
public class PreferenceInfo {

    @Column(name = "preference_id")
    private Long preferenceId;

    @Column(name = "is_selected")
    private boolean isSelected;

    public PreferenceInfo() {
    }

    public PreferenceInfo(Long preferenceId, boolean isSelected) {
        this.preferenceId = preferenceId;
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "PreferenceInfo{" +
                "preferenceId=" + preferenceId +
                ", isSelected=" + isSelected +
                '}';
    }
}
