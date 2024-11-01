package com.noblesse.backend.manage.clip.controller;

import com.noblesse.backend.manage.clip.dto.ReportedClipDTO;
import com.noblesse.backend.manage.clip.dto.ReportedClipDetailDTO;
import com.noblesse.backend.manage.clip.service.ClipManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/admin/clip")
@RequiredArgsConstructor
@Tag(name = "클립 관리", description = "신고된 클립 관리 API")
public class ClipManagementController {

    private final ClipManagementService clipManagementService;

    @Operation(summary = "신고된 클립 목록 조회")
    @ApiResponse(responseCode = "200", description = "신고된 클립 목록 조회 성공")
    @GetMapping("/reported")
    // 신고 접수된 모든 회원 목록 조회 + 페이지네이션
    public ResponseEntity<Page<ReportedClipDTO>> getAllReportedClips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "clipReportedAt") String sortBy) {
        return ResponseEntity.ok(clipManagementService.getAllReportedClips(page, size, sortBy));
    }

    @Operation(summary = "신고된 클립 상세 정보 조회")
    @ApiResponse(responseCode = "200", description = "신고된 클립 상세 정보 조회 성공")
    @GetMapping("/reported/{clipId}")
    public ResponseEntity<ReportedClipDetailDTO> getReportedClipDetail(@PathVariable Long clipId) {
        ReportedClipDetailDTO clipDetail = clipManagementService.getReportedClipDetail(clipId);
        return ResponseEntity.ok(clipDetail);
    }

    @Operation(summary = "신고된 클립 삭제")
    @ApiResponse(responseCode = "200", description = "신고된 클립 삭제 성공")
    @DeleteMapping("/{clipId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteClipByAdmin(@PathVariable Long clipId) {
        log.debug("Received delete request for clipId: {}", clipId);

        if (clipId == null) {
            log.warn("Attempt to delete clip with null ID");
            return ResponseEntity.badRequest().body("Invalid clip ID: null");
        }

        try {
            clipManagementService.deleteClipAndReports(clipId);
            log.info("Successfully deleted clip with ID: {}", clipId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            log.warn("Attempt to delete non-existent clip with ID: {}", clipId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while deleting clip with ID: {}", clipId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting clip");
        }
    }
}