package com.noblesse.backend.clip.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "ClipReport")
@Table(name = "clip_report")
public class ClipReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLIP_REPORT_ID")
    private Long clipReportId;

    @Column(name = "REPORT_CATEGORY_ID")
    private Long reportCategoryId;

    @Column(name = "CLIP_REPORT_TITLE")
    private String clipReportTitle;

    @Column(name = "CLIP_REPORT_CONTENT")
    private String clipReportContent;

    @CreationTimestamp
    @Column(name = "CLIP_REPORTED_AT")
    private LocalDateTime clipReportedAt; // 클립 정지 일자

    @Column(name = "USER_ID")
    private Long userId;

//    @Column(name = "CLIP_ID")
//    private Long clipId;

    @ManyToOne
    @JoinColumn(name = "CLIP_ID")
    private Clip clip;

    protected ClipReport() {}

    public ClipReport(Long reportCategoryId, String clipReportTitle, String clipReportContent, Long userId, Clip clip) {
        this.reportCategoryId = reportCategoryId;
        this.clipReportTitle = clipReportTitle;
        this.clipReportContent = clipReportContent;
        this.userId = userId;
        this.clip = clip;
    }

    public void setReportCategoryId(Long reportCategoryId) {
        this.reportCategoryId = reportCategoryId;
    }

    public void setClipReportTitle(String clipReportTitle) {
        this.clipReportTitle = clipReportTitle;
    }

    public void setClipReportContent(String clipReportContent) {
        this.clipReportContent = clipReportContent;
    }

    public LocalDateTime getClipReportedAt() {
        return clipReportedAt;
    }

    public String getClipReportTitle() {
        return clipReportTitle;
    }

    public void setClipReportedAt(LocalDateTime clipReportedAt) {
        this.clipReportedAt = clipReportedAt;
    }

    public Long getClipReportId() {
        return clipReportId;
    }

    public Long getReportCategoryId() {
        return reportCategoryId;
    }

    public String getClipReportContent() {
        return clipReportContent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClipId() {
        return clip != null ? clip.getClipId() : null;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    @Override
    public String toString() {
        return "ClipReport{" +
                "clipReportId=" + clipReportId +
                ", reportCategoryId=" + reportCategoryId +
                ", clipReportContent='" + clipReportContent + '\'' +
                ", userId=" + userId +
                ", clipId=" + getClipId() +
//                ", clipId=" + clipId +
                '}';
    }
}
