package com.noblesse.backend.preference.repository;

import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.domain.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    @Query("SELECT up.preferenceInfoList FROM UserPreference up WHERE up.userId = :userId")
    List<PreferenceInfo> findPreferenceInfoListByUserId(@Param("userId") Long userId);

    Optional<UserPreference> findUserPreferenceByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM UserPreference up JOIN up.preferenceInfoList p WHERE p.isSelected = true AND up.userId = :userId")
    List<PreferenceInfo> findSelectedPreferenceInfoListByUserId(@Param("userId") Long userId);

}
