package com.noblesse.backend.manage.clip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReportedClipDTO {
    private List<Long> clipIds;
    private String email;
    private List<String> clipTitles;
    private List<String> clipUrls;
    private LocalDateTime clipReportedAt;
    private List<Long> reportedClips;
}
