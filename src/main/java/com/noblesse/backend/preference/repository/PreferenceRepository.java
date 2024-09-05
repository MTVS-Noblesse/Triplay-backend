package com.noblesse.backend.preference.repository;

import com.noblesse.backend.preference.domain.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    @Query("SELECT p FROM Preference p WHERE p.preferenceId = :preferenceId")
    Optional<Preference> findPreferenceByPreferenceId(@Param("preferenceId") Long preferenceId);

    @Query("SELECT p.preferenceName FROM Preference p WHERE p.preferenceId = :preferenceId")
    String findPreferenceNameByPreferenceId(@Param("preferenceId") Long preferenceId);

    @Query("SELECT p.preferenceId FROM Preference p")
    List<Long> findAllPreferenceId();
}
