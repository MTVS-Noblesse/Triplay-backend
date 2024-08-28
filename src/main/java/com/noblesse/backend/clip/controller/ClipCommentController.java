package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.domain.ClipComment;
import com.noblesse.backend.clip.dto.ClipCommentRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipComment")
public class ClipCommentController {
    private final ClipCommentService clipCommentService;

    public ClipCommentController(ClipCommentService clipCoCommentService) {
        this.clipCommentService = clipCoCommentService;
    }

    @Operation(summary = "클립 댓글 상세 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipComment.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{clipCommentId}")
    public ResponseEntity<?> findClipCommentByClipCommentId(@PathVariable Long clipCommentId) {
        return ResponseEntity.ok(clipCommentService.findClipCommentByClipCommentId(clipCommentId));
    }

    @Operation(summary = "클립에 따른 댓글 전체 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipComment.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{clipId}")
    public ResponseEntity<?> findAllClipCommentByClipId(@PathVariable Long clipId) {
        return ResponseEntity.ok(clipCommentService.findAllClipCommentByClipId(clipId));
    }

    @Operation(summary = "클립에 댓글 추가")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipComment.class)))
    @PostMapping("/{clipId}")
    public ResponseEntity<?> registClipComment(@ModelAttribute ClipCommentRegistRequestDTO clipCommentRegistRequestDTO) {
        clipCommentService.registClipComment(clipCommentRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 댓글 수정")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipComment.class)))
    @PutMapping("/{clipCommentId}")
    public ResponseEntity<?> updateClipCoComment(
            @PathVariable Long clipCommentId,
            @ModelAttribute String clipCommentContent) {

        clipCommentService.updateClipComment(clipCommentId, clipCommentContent);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 댓글 삭제")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/{clipCommentId}")
    public ResponseEntity<?> deleteClipCoComment(@PathVariable Long clipCommentId) {
        clipCommentService.deleteClipByClipId(clipCommentId);
        return ResponseEntity.ok().build();
    }
}
