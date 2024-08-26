package com.noblesse.backend.clip.service;

import com.noblesse.backend.clip.domain.ClipReport;
import com.noblesse.backend.clip.dto.ClipReportRegistRequestDTO;
import com.noblesse.backend.clip.repository.ClipReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClipReportService {
    private final ClipReportRepository clipReportRepository;

    public ClipReportService(ClipReportRepository clipReportRepository) {
        this.clipReportRepository = clipReportRepository;
    }

    public ClipReport findClipReportByClipReportId(Long clipReportId) {
        return clipReportRepository.findClipReportByClipReportId(clipReportId);
    }

    public List<ClipReport> findClipReportsByClipId(Long clipId) {
        return clipReportRepository.findClipReportsByClipId(clipId);
    }

    public List<ClipReport> findAll() {
        return clipReportRepository.findAll();
    }

    public void registClipReport(ClipReportRegistRequestDTO clipReportRegistRequestDTO) {
        clipReportRepository.save(new ClipReport(
                clipReportRegistRequestDTO.getReportCategoryId(),
                clipReportRegistRequestDTO.getClipReportTitle(),
                clipReportRegistRequestDTO.getClipReportContent(),
                clipReportRegistRequestDTO.getUserId(),
                clipReportRegistRequestDTO.getClipId()
        ));
    }

    // UPDATE 문은 따로 필요 없을 듯, 신고 내용을 수정하는 정도에서는 제목, 내용 정도..?
    @Transactional
    public void updateClipReport(Long ClipReportId, String newTitle, String newContent) {
        ClipReport clipReport = findClipReportByClipReportId(ClipReportId);
        if (clipReport != null) {
            clipReport.setClipReportTitle(newTitle);
            clipReport.setClipReportContent(newContent);
        }
    }

    @Transactional
    public void deleteClipReportByClipReportId(Long clipReportId) {
        clipReportRepository.deleteById(clipReportId);
    }
}
