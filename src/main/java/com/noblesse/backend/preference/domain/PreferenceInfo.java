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

    @ManyToOne
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @Column(name = "is_selected")
    private boolean isSelected;

    public PreferenceInfo() {
    }

    public PreferenceInfo(Preference preference, boolean isSelected) {
        this.preference = preference;
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "PreferenceInfo{" +
                "preference=" + preference +
                ", isSelected=" + isSelected +
                '}';
    }
}
