package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.domain.ClipReport;
import com.noblesse.backend.clip.dto.ClipReportRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipReport")
public class ClipReportController {
    private final ClipReportService clipReportService;

    public ClipReportController(ClipReportService clipReportService) {
        this.clipReportService = clipReportService;
    }

    @Operation(summary = "모든 클립 신고 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipReport.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping
    public ResponseEntity<?> findAllClipReports() {
        return ResponseEntity.ok(clipReportService.findAll());
    }

    @Operation(summary = "클립에 따른 전체 신고 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipReport.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{clipId}")
    public ResponseEntity<?> findClipReportsByClipId(@PathVariable Long clipId) {
        return ResponseEntity.ok(clipReportService.findClipReportsByClipId(clipId));
    }

    @GetMapping("/{clipReportId}")
    public ResponseEntity<?> findClipReportByClipReportId(@PathVariable Long clipReportId) {
        return ResponseEntity.ok(clipReportService.findClipReportByClipReportId(clipReportId));
    }

    @Operation(summary = "클립에 따른 신고 추가")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipReport.class)))
    @PostMapping
    public ResponseEntity<?> registClipReport(
            @ModelAttribute ClipReportRegistRequestDTO clipReportRegistRequestDTO) {
        clipReportService.registClipReport(clipReportRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 신고 수정")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipReport.class)))
    @PutMapping("/{clipReportId}")
    public ResponseEntity<?> updateClipReportByClipReportId(
            @PathVariable Long clipReportId,
            @ModelAttribute String newTitle,
            @ModelAttribute String newContent) {

        clipReportService.updateClipReport(clipReportId, newTitle, newContent);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 신고 삭제")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{clipReportId}")
    public ResponseEntity<?> deleteClipReportByClipReportId(@PathVariable Long clipReportId) {
        clipReportService.deleteClipReportByClipReportId(clipReportId);
        return ResponseEntity.ok().build();
    }
}
