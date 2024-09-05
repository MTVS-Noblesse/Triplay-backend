package com.noblesse.backend.preference.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="user_preference")
@Getter @Setter
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_preference_id")
    private Long userPreferenceId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "preference_info_list",
            joinColumns = @JoinColumn(name = "user_preference_id")
    )
    private List<PreferenceInfo> preferenceInfoList;

    @Column(name="user_id")
    private Long userId;

    public UserPreference() {}

    public UserPreference(Long userId, List<PreferenceInfo> preferenceInfos) {
        this.userId = userId;
        this.preferenceInfoList = preferenceInfos;
    }

    @Override
    public String toString() {
        return "UserPreference{" +
                "userPreferenceId=" + userPreferenceId +
                ", preferenceInfoList=" + preferenceInfoList +
                ", userId=" + userId +
                '}';
    }
}
