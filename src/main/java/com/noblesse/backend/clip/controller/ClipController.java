package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.domain.Clip;
import com.noblesse.backend.clip.dto.ClipRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clip")
public class ClipController {
    private final ClipService clipService;

    public ClipController(ClipService clipService) {
        this.clipService = clipService;
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

    @Operation(summary = "클립 추가")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clip.class)))
    @PostMapping
    public ResponseEntity<?> registClip(@ModelAttribute ClipRegistRequestDTO clipRegistRequestDTO) {
        clipService.insertClip(clipRegistRequestDTO);
        return ResponseEntity.ok().build();
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
