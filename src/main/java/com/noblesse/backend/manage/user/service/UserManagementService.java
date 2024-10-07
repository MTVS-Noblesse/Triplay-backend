package com.noblesse.backend.manage.user.service;

import com.noblesse.backend.manage.user.dto.UserManagementDTO;
import com.noblesse.backend.manage.user.dto.UserStatusUpdateDTO;
import com.noblesse.backend.manage.user.repository.UserManagementRepository;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.preference.service.PreferenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final PreferenceService preferenceService;

    public Page<UserManagementDTO> getAllUsers(int page, int size, String sortBy) {
        Pageable  pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<OAuthUser> userPage = userManagementRepository.findAll(pageable);
        return userPage.map(this::convertToDTO);
    }

    @Transactional
    public UserManagementDTO updateUserStatus(Long userId, UserStatusUpdateDTO statusUpdateDTO) {
        log.info("Updating user status for userId: {}, new status: {}", userId, statusUpdateDTO.isFired());

        OAuthUser user = userManagementRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        log.info("Current user status - isFired: {}, firedAt: {}", user.isFired(), user.getFiredAt());

        if (statusUpdateDTO.isFired() != user.isFired()) {
            user.setFired(statusUpdateDTO.isFired());
            if (statusUpdateDTO.isFired()) {
                user.setFiredAt(LocalDateTime.now());
                log.info("Suspending user. New firedAt: {}", user.getFiredAt());
            } else {
                user.setFiredAt(null);
                log.info("Reinstating user. firedAt set to null.");
            }

            OAuthUser updatedUser = userManagementRepository.save(user);
            log.info("User status updated. New status - isFired: {}, firedAt: {}", updatedUser.isFired(), updatedUser.getFiredAt());
            return convertToDTO(updatedUser);
        } else {
            log.info("No change in user status. Skipping update.");
            return convertToDTO(user);
        }
    }

    private UserManagementDTO convertToDTO(OAuthUser user) {
        UserManagementDTO dto = new UserManagementDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        dto.setProvider(user.getProvider());
        dto.setFiredAt(user.getFiredAt());
        dto.setFired(user.isFired());

        List<String> preferences = preferenceService.findSelectedUserPreferenceList(user.getId())
                .stream()
                .map(preferenceInfo -> preferenceService.getPreferenceBypreferenceId(preferenceInfo.getPreferenceId()).getPreferenceName())
                .collect(Collectors.toList());
        dto.setPreferences(preferences);

        return dto;
    }
}
