package com.noblesse.backend.preference.service;

import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.domain.UserPreference;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PreferenceServiceImpl implements PreferenceService{

    @Autowired
    PreferenceRepository preferenceRepository;

    @Autowired
    UserPreferenceRepository userPreferenceRepository;

    @Override
    public Preference getPreferenceBypreferenceId(Long preferenceId) {
        return preferenceRepository.findPreferenceByPreferenceId(preferenceId).orElseThrow(() -> new IllegalArgumentException("not found preference id: " + preferenceId));
    }

    public void registerPreference(String preferenceName){
        preferenceRepository.save(new Preference(preferenceName));
    }

    public void updatePreference(NewPreferenceDTO newPreferenceDTO){
        Preference foundPreference = preferenceRepository.findById(newPreferenceDTO.getPreferenceId())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Preference not found for id: " + newPreferenceDTO.getPreferenceId()
                        )
                );
        foundPreference.setPreferenceName(newPreferenceDTO.getNewPreferenceName());
        preferenceRepository.save(foundPreference);
    }

    @Override
    public void updateUserPreferences(List<Long> preferenceIds, Long userId) {
        // UserPreference 조회 시 존재하지 않으면 새로 생성
        UserPreference userPreference = userPreferenceRepository.findUserPreferenceByUserId(userId)
                .orElseGet(() -> {
                    UserPreference newUserPreference = new UserPreference(userId, new ArrayList<>());
                    userPreferenceRepository.save(newUserPreference);
                    return newUserPreference;
                });

        // user_preference_id를 가져옵니다.
        Long userPreferenceId = userPreference.getUserPreferenceId();

        // 기존 preferenceInfoList를 가져옵니다
        List<PreferenceInfo> preferenceInfoList = userPreference.getPreferenceInfoList();

        // 기존 preferenceInfoList의 is_selected 상태 업데이트
        for (PreferenceInfo info : preferenceInfoList) {
            if (preferenceIds.contains(info.getPreferenceId())) {
                info.setSelected(true);
            } else {
                info.setSelected(false);
            }
        }

        // 새로운 취향 추가
        List<Long> allPreferenceIds = preferenceRepository.findAllPreferenceId();

        for (Long id : allPreferenceIds) {
            boolean exists = preferenceInfoList.stream()
                    .anyMatch(info -> info.getPreferenceId().equals(id));

            if (!exists) {
                Preference newPreference = preferenceRepository.findPreferenceByPreferenceId(id)
                        .orElseThrow(() -> new IllegalArgumentException("preference not found: " + id));
                PreferenceInfo newInfo = new PreferenceInfo(newPreference.getPreferenceId(), false);
                preferenceInfoList.add(newInfo);
            }
        }

        // 삭제된 취향 처리
        preferenceInfoList.removeIf(info -> !allPreferenceIds.contains(info.getPreferenceId()));

        // 업데이트된 preferenceInfoList 저장
        userPreferenceRepository.save(userPreference);
    }

    @Override
    public List<PreferenceInfo> findSelectedUserPreferenceList(Long userId) {
        return userPreferenceRepository.findSelectedPreferenceInfoListByUserId(userId);
    }

    @Override
    public void deletePreference(Long id) {
        preferenceRepository.deleteById(id);
    }
}
