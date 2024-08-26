package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.dto.ClipReportRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipReport")
public class ClipReportController {
    private final ClipReportService clipReportService;

    public ClipReportController(ClipReportService clipReportService) {
        this.clipReportService = clipReportService;
    }

    @GetMapping
    public ResponseEntity<?> findAllClipReports() {
        return ResponseEntity.ok(clipReportService.findAll());
    }

    @GetMapping("/{clipId}")
    public ResponseEntity<?> findClipReportsByClipId(@PathVariable Long clipId) {
        return ResponseEntity.ok(clipReportService.findClipReportsByClipId(clipId));
    }

    @PostMapping
    public ResponseEntity<?> registClipReport(
            @ModelAttribute ClipReportRegistRequestDTO clipReportRegistRequestDTO) {
        clipReportService.registClipReport(clipReportRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{clipReportId}")
    public ResponseEntity<?> updateClipReportByClipReportId(
            @PathVariable Long clipReportId, @ModelAttribute String newTitle, @ModelAttribute String newContent) {
        clipReportService.updateClipReport(clipReportId, newTitle, newContent);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clipReportId}")
    public ResponseEntity<?> deleteClipReportByClipReportId(@PathVariable Long clipReportId) {
        clipReportService.deleteClipReportByClipReportId(clipReportId);
        return ResponseEntity.ok().build();
    }
}
