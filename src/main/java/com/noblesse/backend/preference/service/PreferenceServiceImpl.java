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

    // 처음에 회원가입 할 때 유저 취향 리스트 선택할텐데, 그때 취향들 등록하는 로직도 업데이트랑 똑같아서 이 메소드 쓰면 됨.
    public void updateUserPreferences(List<Long> preferenceIds, Long userId) {
        // UserPreference 조회 시 존재하지 않으면 새로 생성
        UserPreference userPreference = userPreferenceRepository.findUserPreferenceByUserId(userId)
                .orElseGet(() -> {
                    UserPreference newUserPreference = new UserPreference(userId, new ArrayList<>());
                    userPreferenceRepository.save(newUserPreference);
                    return newUserPreference;
                });

        // 기존 preferenceInfoList를 가져옵니다
        List<PreferenceInfo> preferenceInfoList = userPreference.getPreferenceInfoList();

        // 선택된 preferenceIds에 따라 선택 여부를 업데이트
        for (PreferenceInfo info : preferenceInfoList) {
            boolean isSelected = preferenceIds.contains(info.getPreferenceId());
            info.setSelected(isSelected);
        }

        // 새로운 취향이 추가되었을 때를 위해 기존 preferenceInfoList에 반영
        List<Long> allPreferenceIds = preferenceRepository.findAllPreferenceId();

        for (Long id : allPreferenceIds) {
            boolean isExist = preferenceInfoList.stream()
                    .anyMatch(info -> info.getPreferenceId().equals(id));
            if (!isExist) {
                Preference newPreference = preferenceRepository.findPreferenceByPreferenceId(id)
                        .orElseThrow(() -> new IllegalArgumentException("preference not found " + id));
                PreferenceInfo newInfo = new PreferenceInfo(newPreference.getPreferenceId(), false);
                preferenceInfoList.add(newInfo);
            }
        }

        // 삭제된 취향 처리 (기존 preferenceInfoList에서 삭제)
        preferenceInfoList.removeIf(info -> !allPreferenceIds.contains(info.getPreferenceId()));

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
