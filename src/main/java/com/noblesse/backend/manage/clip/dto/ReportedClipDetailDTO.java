package com.noblesse.backend.manage.clip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReportedClipDetailDTO {
    private Long clipId;
    private String clipTitle;
    private String email;
    private String clipUrl;
    private int reportCount;
    private List<String> reportContents;
}
