package com.noblesse.backend.preference.service;

import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.domain.UserPreference;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreferenceServiceImpl implements PreferenceService{

    @Autowired
    PreferenceRepository preferenceRepository;

    @Autowired
    UserPreferenceRepository userPreferenceRepository;

    public void registPreference(String preferenceName){
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
    public void updateUserPreferences(List<Long> preferenceIds, Long userId){
        UserPreference userPreference = userPreferenceRepository.findUserPreferenceByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserPreference not found for userId: " + userId));

//        LAZY 로딩: getPreferenceInfos() 호출 시에 데이터베이스에서 로드됨
        List<PreferenceInfo> preferenceInfoList = userPreference.getPreferenceInfoList();

        for(PreferenceInfo info : preferenceInfoList){
            boolean isSelected = preferenceIds.contains(info.getPreference().getPreferenceId());
            info.setSelected(isSelected);
        }

        // 만약 취향 리스트에 새로운 취향이 추가되었다면 기존의 유저 preferenceInfoList에 반영함.
        List<Long> allPreferenceIds = preferenceRepository.findAllPreferenceId();

        for(Long id : allPreferenceIds){
            boolean isExist = preferenceInfoList.stream()
                    .anyMatch(info -> info.getPreference().getPreferenceId().equals(id));
            if(!isExist){
                Preference newPreference = preferenceRepository.findPreferenceByPreferenceId(id)
                        .orElseThrow(() -> new IllegalArgumentException("preference not found " + id));

                PreferenceInfo newInfo = new PreferenceInfo(newPreference, false);
                preferenceInfoList.add(newInfo);
            }
        }

        // 5. 삭제된 취향을 처리 (기존 preferenceInfoList에서 삭제)
        preferenceInfoList.removeIf(info -> !allPreferenceIds.contains(info.getPreference().getPreferenceId()));

        userPreferenceRepository.save(userPreference);
    }

    public List<PreferenceInfo> findSelectedUserPreferenceList(Long userId) {
        return userPreferenceRepository.findSelectedPreferenceInfoListByUserId(userId);
    }
}
