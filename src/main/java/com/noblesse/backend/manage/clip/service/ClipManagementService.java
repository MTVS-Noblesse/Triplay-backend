package com.noblesse.backend.manage.clip.service;

import com.noblesse.backend.clip.domain.Clip;
import com.noblesse.backend.clip.domain.ClipReport;
import com.noblesse.backend.clip.repository.ClipRepository;
import com.noblesse.backend.clip.repository.ClipReportRepository;
import com.noblesse.backend.file.service.FileService;
import com.noblesse.backend.manage.clip.dto.ReportedClipDTO;
import com.noblesse.backend.manage.clip.dto.ReportedClipDetailDTO;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ClipManagementService {

    private final ClipRepository clipRepository;
    private final ClipReportRepository clipReportRepository;
    private final OAuthRepository oAuthRepository;
    private final FileService fileService;

    public Page<ReportedClipDTO> getAllReportedClips(int page, int size, String sortBy) {
        log.debug("Entering getAllReportedClips method");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ClipReport> reportedClipPage = clipReportRepository.findAll(pageable);
        log.debug("Found {} clip reports", reportedClipPage.getTotalElements());

        Map<String, ReportedClipDTO> userClipsMap = new HashMap<>();

        for (ClipReport report : reportedClipPage) {
            log.debug("Processing report for clipId: {}", report.getClipId());
            Clip clip = clipRepository.findById(report.getClipId())
                    .orElseThrow(() -> new EntityNotFoundException("Clip not found for id: " + report.getClipId()));

            OAuthUser user = oAuthRepository.findById(clip.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + clip.getUserId()));

            String email = user.getEmail();
            String firebaseUrl = fileService.findImageDownloadLinkByFileUrl(clip.getClipUrl());
            log.debug("Firebase URL for clip {}: {}", clip.getClipId(), firebaseUrl);

            if (!userClipsMap.containsKey(email)) {
                userClipsMap.put(email, new ReportedClipDTO(
                        new ArrayList<>(),
                        email,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        report.getClipReportedAt(),
                        new ArrayList<>()
                ));
                log.debug("Created new DTO for email: {}", email);
            }

            ReportedClipDTO dto = userClipsMap.get(email);
            dto.getClipIds().add(clip.getClipId());
            dto.getClipTitles().add(clip.getClipTitle());
            dto.getClipUrls().add(firebaseUrl);
            dto.getReportedClips().add(clip.getClipId());
            log.debug("Added clipId {} to reportedClips for email: {}", clip.getClipId(), email);

            if (dto.getClipReportedAt().isBefore(report.getClipReportedAt())) {
                dto.setClipReportedAt(report.getClipReportedAt());
                log.debug("Updated clipReportedAt for email: {}", email);
            }
        }

        List<ReportedClipDTO> resultList = new ArrayList<>(userClipsMap.values());
        log.debug("Final result list size: {}", resultList.size());

        // Sort the result list based on the sortBy parameter
        if ("email".equals(sortBy)) {
            resultList.sort(Comparator.comparing(ReportedClipDTO::getEmail));
        } else {
            resultList.sort(Comparator.comparing(ReportedClipDTO::getClipReportedAt).reversed());
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());

        Page<ReportedClipDTO> result = new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
        log.debug("Returning page with {} elements", result.getContent().size());
        return result;
    }

    public ReportedClipDetailDTO getReportedClipDetail(Long clipId) {
        log.debug("### getReportedClipDetail 진입...");
        log.debug("Get reported clip detail for clipId {}", clipId);
        Clip clip = clipRepository.findClipByClipId(clipId);
        if (clip == null) {
            throw new EntityNotFoundException("Clip not found");
        }

        OAuthUser user = oAuthRepository.findById(clip.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<ClipReport> reports = clipReportRepository.findClipReportsByClip_ClipId(clipId);

        String clipUrl = fileService.findImageDownloadLinkByFileUrl(clip.getClipUrl());
        log.debug("clipUrl: {}", clipUrl);

        return new ReportedClipDetailDTO(
                clip.getClipId(),
                clip.getClipTitle(),
                user.getEmail(),
                clipUrl,
                reports.size(),
                reports.stream().map(ClipReport::getClipReportContent).collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteClipAndReports(Long clipId) {
        log.debug("Attempting to delete clip and reports for clipId: {}", clipId);

        try {
            Clip clip = clipRepository.findById(clipId)
                    .orElseThrow(() -> new EntityNotFoundException("Clip not found with id: " + clipId));

            log.debug("Found clip: {}", clip);

            clipReportRepository.deleteByClip(clip);
            log.debug("Deleted all reports for clipId: {}", clipId);

            clipRepository.delete(clip);
            log.debug("Deleted clip with id: {}", clipId);

            // 파일 시스템에서 관련 파일 삭제 (만약 있다면)
            fileService.deleteClipFileByClipId(clipId);
            log.debug("Deleted associated files for clipId: {}", clipId);

        } catch (Exception e) {
            log.error("Error occurred while deleting clip and reports for clipId: {}", clipId, e);
            throw e;
        }
    }
}