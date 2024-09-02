package com.noblesse.backend.clip.controller;

import com.noblesse.backend.clip.dto.ClipRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clip")
public class ClipController {
    private final ClipService clipService;

    public ClipController(ClipService clipService) {
        this.clipService = clipService;
    }

    @GetMapping
    public ResponseEntity<?> findAllClip() {
        return ResponseEntity.ok(clipService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findClipById(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(clipService.findClipByClipId(userId));
    }

    @PostMapping
    public ResponseEntity<?> registClip(@ModelAttribute ClipRegistRequestDTO clipRegistRequestDTO) {
        clipService.insertClip(clipRegistRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{clipId}")
    public ResponseEntity<?> updateClipByClipId(@PathVariable Long clipId) {
        clipService.updateClipByClipIdForExposeYN(clipId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clipId}")
    public ResponseEntity<?> deleteClipByClipId(@PathVariable Long clipId) {
        clipService.deleteClipByClipId(clipId);
        return ResponseEntity.ok().build();
    }
}
