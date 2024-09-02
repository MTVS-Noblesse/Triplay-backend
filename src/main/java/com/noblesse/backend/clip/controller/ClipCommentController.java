package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.dto.ClipCommentRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipComment")
public class ClipCommentController {
    private final ClipCommentService clipCommentService;

    public ClipCommentController(ClipCommentService clipCoCommentService) {
        this.clipCommentService = clipCoCommentService;
    }

    @GetMapping("/{clipCommentId}")
    public ResponseEntity<?> findClipCommentByClipCommentId(@PathVariable Long clipCommentId) {
        return ResponseEntity.ok(clipCommentService.findClipCommentByClipCommentId(clipCommentId));
    }

    @GetMapping("/{clipId}")
    public ResponseEntity<?> findAllClipCommentByClipId(@PathVariable Long clipId) {
        return ResponseEntity.ok(clipCommentService.findAllClipCommentByClipId(clipId));
    }

    @PostMapping
    public ResponseEntity<?> registClipComment(@ModelAttribute ClipCommentRegistRequestDTO clipCommentRegistRequestDTO) {
        clipCommentService.registClipComment(clipCommentRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{clipCommentId}")
    public ResponseEntity<?> updateClipCoComment(
            @PathVariable Long clipCommentId,
            @ModelAttribute String clipCommentContent) {

        clipCommentService.updateClipComment(clipCommentId, clipCommentContent);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clipCommentId}")
    public ResponseEntity<?> deleteClipCoComment(@PathVariable Long clipCommentId) {
        clipCommentService.deleteClipByClipId(clipCommentId);
        return ResponseEntity.ok().build();
    }
}
