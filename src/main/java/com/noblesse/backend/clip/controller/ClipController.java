package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.domain.Clip;
import com.noblesse.backend.clip.dto.ClipImageUploadRequestDTO;
import com.noblesse.backend.clip.dto.ClipRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipService;
import com.noblesse.backend.oauth2.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/clip")
public class ClipController {
    private final ClipService clipService;
    private final JwtUtil jwtUtil;

    public ClipController(ClipService clipService, JwtUtil jwtUtil) {
        this.clipService = clipService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "클립 내용 전체 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clip.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping
    public ResponseEntity<?> findAllClip() {
        return ResponseEntity.ok(clipService.findAll());
    }

    @Operation(summary = "사용자가 작성한 클립 목록 조회")
    @GetMapping("/user")
    public ResponseEntity<?> getUserClips(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return ResponseEntity.ok(clipService.findClipsByUserId(userId));
    }

    @Operation(summary = "클립 상세 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clip.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{clipId}")
    public ResponseEntity<?> findClipById(@PathVariable("clipId") Long clipId) {
        return ResponseEntity.ok(clipService.findClipByClipId(clipId));
    }

    @Operation(summary = "클립 이미지 업로드")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clip.class)))
    @PostMapping
    public ResponseEntity<?> registClipImageFile(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @ModelAttribute ClipImageUploadRequestDTO clipImageUploadRequestDTO) throws IOException {

        String token = authorizationHeader.substring(7); // 앞의 "Bearer " 제거
        Long userId = jwtUtil.extractUserId(token);
        Long clipId = clipService.uploadImageFiles(clipImageUploadRequestDTO, userId);
        return ResponseEntity.ok(clipId);
    }

    @Operation(summary = "클립 추가 정보 저장")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clip.class)))
    @PatchMapping("/{clipId}")
    public ResponseEntity<?> registClipInfo(
            @PathVariable(name = "clipId") long clipId,
            @ModelAttribute ClipRegistRequestDTO clipRegistRequestDTO) {
        try {
            clipService.insertClip(clipRegistRequestDTO, clipId);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            // 로그 기록
            System.err.println("클립 추가 정보 등록 중 오류 발생: " + e.getMessage());
            // 클라이언트에게 오류 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("클립 추가 정보 등록 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "클립을 비공개/공개로 수정")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clip.class)))
    @PutMapping("/{clipId}")
    public ResponseEntity<?> updateClipByClipId(@PathVariable Long clipId) {
        clipService.updateClipByClipIdForExposeYN(clipId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 삭제")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{clipId}")
    public ResponseEntity<?> deleteClipByClipId(@PathVariable Long clipId) {
        clipService.deleteClipByClipId(clipId);
        return ResponseEntity.ok().build();
    }
}
