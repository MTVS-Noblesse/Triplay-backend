package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.dto.ClipCoCommentRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipCoCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipCoComment")
public class ClipCoCommentController {
    private final ClipCoCommentService clipCoCommentService;

    public ClipCoCommentController(ClipCoCommentService clipCoCommentService) {
        this.clipCoCommentService = clipCoCommentService;
    }

    @GetMapping("/{clipCoCommentId}")
    public ResponseEntity<?> findClipCoCommentByClipCoCommentId(@PathVariable Long clipCoCommentId) {
        return ResponseEntity.ok(clipCoCommentService.findAllClipCoCOmmentByClipCommentId(clipCoCommentId));
    }

    @GetMapping("/{clipCommentId}")
    public ResponseEntity<?> findAllClipCoCommentByClipCommentId(@PathVariable Long clipCommentId) {
        return ResponseEntity.ok(clipCoCommentService.findAllClipCoCOmmentByClipCommentId(clipCommentId));
    }

    @PostMapping("/{clipCommentId}")
    public ResponseEntity<?> registClipCoComment(@ModelAttribute ClipCoCommentRegistRequestDTO clipCoCommentRegistRequestDTO) {
        clipCoCommentService.registClipCoComment(clipCoCommentRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{clipCoCommentId}")
    public ResponseEntity<?> updateClipCoComment(
            @PathVariable Long clipCoCommentId,
            @ModelAttribute String clipCoCommentContent) {

        clipCoCommentService.updateClipCoCommentByClipCoCommentId(clipCoCommentId, clipCoCommentContent);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clipCoCommentId}")
    public ResponseEntity<?> deleteClipCoComment(@PathVariable Long clipCoCommentId) {
        clipCoCommentService.deleteClipCoCommentByClipCoCommentId(clipCoCommentId);
        return ResponseEntity.ok().build();
    }
}
