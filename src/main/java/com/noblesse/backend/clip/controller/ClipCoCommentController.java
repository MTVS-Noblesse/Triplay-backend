package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.domain.ClipCoComment;
import com.noblesse.backend.clip.dto.ClipCoCommentRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipCoCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipCoComment")
public class ClipCoCommentController {
    private final ClipCoCommentService clipCoCommentService;

    public ClipCoCommentController(ClipCoCommentService clipCoCommentService) {
        this.clipCoCommentService = clipCoCommentService;
    }

    @Operation(summary = "클립 대댓글 상세 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipCoComment.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{clipCoCommentId}")
    public ResponseEntity<?> findClipCoCommentByClipCoCommentId(@PathVariable Long clipCoCommentId) {
        return ResponseEntity.ok(clipCoCommentService.findAllClipCoCOmmentByClipCommentId(clipCoCommentId));
    }

    @Operation(summary = "클립 댓글에 따른 대댓글 전체 조회")
    @Tag(name = "Clip Query")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipCoComment.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{clipCommentId}")
    public ResponseEntity<?> findAllClipCoCommentByClipCommentId(@PathVariable Long clipCommentId) {
        return ResponseEntity.ok(clipCoCommentService.findAllClipCoCOmmentByClipCommentId(clipCommentId));
    }

    @Operation(summary = "클립 댓글에 대댓글 추가")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipCoComment.class)))
    @PostMapping("/{clipCommentId}")
    public ResponseEntity<?> registClipCoComment(@ModelAttribute ClipCoCommentRegistRequestDTO clipCoCommentRegistRequestDTO) {
        clipCoCommentService.registClipCoComment(clipCoCommentRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 대댓글 수정")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipCoComment.class)))
    @PutMapping("/{clipCoCommentId}")
    public ResponseEntity<?> updateClipCoComment(
            @PathVariable Long clipCoCommentId,
            @ModelAttribute String clipCoCommentContent) {

        clipCoCommentService.updateClipCoCommentByClipCoCommentId(clipCoCommentId, clipCoCommentContent);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "클립 대댓글 삭제")
    @Tag(name = "Clip Command")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClipCoComment.class)))
    @DeleteMapping("/{clipCoCommentId}")
    public ResponseEntity<?> deleteClipCoComment(@PathVariable Long clipCoCommentId) {
        clipCoCommentService.deleteClipCoCommentByClipCoCommentId(clipCoCommentId);
        return ResponseEntity.ok().build();
    }
}
