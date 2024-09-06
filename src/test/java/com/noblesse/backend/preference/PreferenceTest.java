package com.noblesse.backend.preference;

import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import com.noblesse.backend.preference.domain.Preference;
import com.noblesse.backend.preference.domain.PreferenceInfo;
import com.noblesse.backend.preference.domain.UserPreference;
import com.noblesse.backend.preference.dto.NewPreferenceDTO;
import com.noblesse.backend.preference.repository.PreferenceRepository;
import com.noblesse.backend.preference.repository.UserPreferenceRepository;
import com.noblesse.backend.preference.service.PreferenceServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PreferenceTest {

    @Autowired
    PreferenceRepository preferenceRepository;

    @Autowired
    UserPreferenceRepository userPreferenceRepository;

    @Autowired
    PreferenceServiceImpl preferenceServiceImpl;

    @PersistenceContext
    private EntityManager entityManager;


    @BeforeEach
    @Transactional
    void beforeEach() {
        // 1. AUTO_INCREMENT 값 초기화 쿼리 실행
        entityManager.createNativeQuery("ALTER TABLE preference AUTO_INCREMENT = 1").executeUpdate();
        entityManager.flush();

        if (preferenceRepository.count() == 0) {
            preferenceRepository.save(new Preference("신나는 액티비티"));
            preferenceRepository.save(new Preference("SNS에서 본 곳"));
            preferenceRepository.save(new Preference("맛있는 여행"));
            preferenceRepository.save(new Preference("쇼핑은 필수"));
            preferenceRepository.save(new Preference("예술적 감각을 깨우다"));
        }

        if(userPreferenceRepository.count() <= 0) {
            List<PreferenceInfo> newPreferenceInfos = new ArrayList<>();
            List<Preference> allPreferences = preferenceRepository.findAll();
            for(Preference p : allPreferences) {
                newPreferenceInfos.add(new PreferenceInfo(p, false));  // 기본 선택되지 않음
            }
            userPreferenceRepository.save(new UserPreference(1L, newPreferenceInfos));
        }
    }


    @DisplayName("취향 리스트에 새로운 취향 등록 테스트")
    @Test
    @Transactional
    void preferenceRegistTest() {
        preferenceServiceImpl.registerPreference("새로 추가된 취향");

        Assertions.assertThat(preferenceRepository.count()).isEqualTo(6);
    }


    @DisplayName("취향 수정 테스트")
    @Test
    @Transactional
    void preferenceUpdateTest() {
        Preference foundPreference1 = preferenceRepository.findPreferenceByPreferenceId(1L)
                .orElseThrow(() -> new IllegalArgumentException("not found preference id: " + 1L));
        NewPreferenceDTO newPreferenceDTO = new NewPreferenceDTO(foundPreference1.getPreferenceId(), "수정된 취향 이름");
        preferenceServiceImpl.updatePreference(newPreferenceDTO);

        Preference foundPreference2 = preferenceRepository.findPreferenceByPreferenceId(1L)
                .orElseThrow(() -> new IllegalArgumentException("not found preference id: " + 1L));
        Assertions.assertThat(foundPreference2.getPreferenceName()).isEqualTo("수정된 취향 이름");
    }


    @DisplayName("유저 취향 등록 테스트")
    @Test
    @Transactional
    void userPreferenceRegistTest() {
        List<Long> selectedPreferenceIds = new ArrayList<>();
        selectedPreferenceIds.add(1L);
        selectedPreferenceIds.add(3L);
        selectedPreferenceIds.add(5L);

        preferenceServiceImpl.updateUserPreferences(selectedPreferenceIds, 1L);

        List<PreferenceInfo> preferenceInfoList = userPreferenceRepository.findPreferenceInfoListByUserId(1L);

        Assertions.assertThat(preferenceInfoList.get(0).isSelected()).isEqualTo(true);
        Assertions.assertThat(preferenceInfoList.get(1).isSelected()).isEqualTo(false);
        Assertions.assertThat(preferenceInfoList.get(2).isSelected()).isEqualTo(true);
        Assertions.assertThat(preferenceInfoList.get(3).isSelected()).isEqualTo(false);
        Assertions.assertThat(preferenceInfoList.get(4).isSelected()).isEqualTo(true);

        Assertions.assertThat(preferenceInfoList.get(0).getPreference().getPreferenceName()).isEqualTo("신나는 액티비티");
        Assertions.assertThat(preferenceInfoList.get(1).getPreference().getPreferenceName()).isEqualTo("SNS에서 본 곳");
        Assertions.assertThat(preferenceInfoList.get(2).getPreference().getPreferenceName()).isEqualTo("맛있는 여행");
        Assertions.assertThat(preferenceInfoList.get(3).getPreference().getPreferenceName()).isEqualTo("쇼핑은 필수");
        Assertions.assertThat(preferenceInfoList.get(4).getPreference().getPreferenceName()).isEqualTo("예술적 감각을 깨우다");

    }


    @DisplayName("유저 취향 수정 테스트")
    @Test
    @Transactional
    void userPreferenceUpdateTest() {

        List<Long> selectedPreferenceIds = new ArrayList<>();
        selectedPreferenceIds.add(1L);
        selectedPreferenceIds.add(3L);
        selectedPreferenceIds.add(5L);

        preferenceServiceImpl.updateUserPreferences(selectedPreferenceIds, 1L);

        List<PreferenceInfo> preferenceInfoList = userPreferenceRepository.findPreferenceInfoListByUserId(1L);

        Assertions.assertThat(preferenceInfoList.get(0).isSelected()).isEqualTo(true);
        Assertions.assertThat(preferenceInfoList.get(1).isSelected()).isEqualTo(false);
        Assertions.assertThat(preferenceInfoList.get(2).isSelected()).isEqualTo(true);
        Assertions.assertThat(preferenceInfoList.get(3).isSelected()).isEqualTo(false);
        Assertions.assertThat(preferenceInfoList.get(4).isSelected()).isEqualTo(true);

        Assertions.assertThat(preferenceInfoList.get(0).getPreference().getPreferenceName()).isEqualTo("신나는 액티비티");
        Assertions.assertThat(preferenceInfoList.get(1).getPreference().getPreferenceName()).isEqualTo("SNS에서 본 곳");
        Assertions.assertThat(preferenceInfoList.get(2).getPreference().getPreferenceName()).isEqualTo("맛있는 여행");
        Assertions.assertThat(preferenceInfoList.get(3).getPreference().getPreferenceName()).isEqualTo("쇼핑은 필수");
        Assertions.assertThat(preferenceInfoList.get(4).getPreference().getPreferenceName()).isEqualTo("예술적 감각을 깨우다");


        // 수정했을 때임

        List<Long> newSelectedPreferenceIds = new ArrayList<>();
        newSelectedPreferenceIds.add(2L);
        newSelectedPreferenceIds.add(5L);

        preferenceServiceImpl.updateUserPreferences(newSelectedPreferenceIds, 1L);
        List<PreferenceInfo> newPreferenceInfoList = userPreferenceRepository.findPreferenceInfoListByUserId(1L);

        Assertions.assertThat(newPreferenceInfoList.get(0).isSelected()).isEqualTo(false);
        Assertions.assertThat(newPreferenceInfoList.get(1).isSelected()).isEqualTo(true);
        Assertions.assertThat(newPreferenceInfoList.get(2).isSelected()).isEqualTo(false);
        Assertions.assertThat(newPreferenceInfoList.get(3).isSelected()).isEqualTo(false);
        Assertions.assertThat(newPreferenceInfoList.get(4).isSelected()).isEqualTo(true);

        Assertions.assertThat(newPreferenceInfoList.get(0).getPreference().getPreferenceName()).isEqualTo("신나는 액티비티");
        Assertions.assertThat(newPreferenceInfoList.get(1).getPreference().getPreferenceName()).isEqualTo("SNS에서 본 곳");
        Assertions.assertThat(newPreferenceInfoList.get(2).getPreference().getPreferenceName()).isEqualTo("맛있는 여행");
        Assertions.assertThat(newPreferenceInfoList.get(3).getPreference().getPreferenceName()).isEqualTo("쇼핑은 필수");
        Assertions.assertThat(newPreferenceInfoList.get(4).getPreference().getPreferenceName()).isEqualTo("예술적 감각을 깨우다");
    }


    @DisplayName("유저가 선택한 취향 리스트 조회 테스트")
    @Test
    @Transactional
    void selectedUserPreferenceFindTest() {
        List<Long> selectedPreferenceIds = new ArrayList<>();
        selectedPreferenceIds.add(1L);
        selectedPreferenceIds.add(3L);
        selectedPreferenceIds.add(5L);

        preferenceServiceImpl.updateUserPreferences(selectedPreferenceIds, 1L);

        List<PreferenceInfo> infoList = preferenceServiceImpl.findSelectedUserPreferenceList(1L);

        Assertions.assertThat(infoList.get(0).getPreference().getPreferenceName()).isEqualTo("신나는 액티비티");
        Assertions.assertThat(infoList.get(1).getPreference().getPreferenceName()).isEqualTo("맛있는 여행");
        Assertions.assertThat(infoList.get(2).getPreference().getPreferenceName()).isEqualTo("예술적 감각을 깨우다");

    }


}
