package com.noblesse.backend.clip.repository;

import com.noblesse.backend.clip.domain.ClipReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipReportRepository extends JpaRepository<ClipReport, Long> {
    ClipReport findClipReportByClipReportId(Long clipReportId);
    List<ClipReport> findClipReportsByClipId(Long clipId);
}
