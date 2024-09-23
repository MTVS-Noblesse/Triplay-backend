package com.noblesse.backend.file.controller;

import com.noblesse.backend.file.service.FileService;
import com.noblesse.backend.oauth2.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file/image")
public class ImageFileController {
    private final FileService fileService;
    private final JwtUtil jwtUtil;

    public ImageFileController(FileService fileService, JwtUtil jwtUtil) {
        this.fileService = fileService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{postId}/new")
    @ResponseBody
    public ResponseEntity<?> registNewImagesByPostId(
            @RequestParam MultipartFile[] files,
            @PathVariable Long postId) throws IOException {

        fileService.insertPostImageFilesByPostId(files, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<?> findImageDownloadLinksByPostId(
            @PathVariable Long postId) {

        return ResponseEntity.ok(fileService.findImageDownloadLinksByPostId(postId));
    }

    @GetMapping("/{postId}/{fileName}")
    @ResponseBody
    public ResponseEntity<?> findImageDownloadLinkByPostIdAndFileName(
            @PathVariable Long postId,
            @PathVariable String fileName) {

        return ResponseEntity.ok(fileService.findImageDownloadLinkByPostIdAndFileName(postId, fileName));
    }

    @DeleteMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<?> deleteImageFileByPostId(@PathVariable Long postId) {
        fileService.deleteImageFilesByPostId(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/{fileName}")
    @ResponseBody
    public ResponseEntity<?> deleteImageFileByPostIdAndFileName(
            @PathVariable Long postId,
            @PathVariable String fileName) {

        fileService.deleteImageFileByPostIdAndFileName(postId, fileName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/profile")
    @ResponseBody
    public ResponseEntity<?> registUserProfileImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody MultipartFile file) throws IOException {

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        fileService.insertProfileImageFileByUserId(file, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/user/profile")
    @ResponseBody
    public ResponseEntity<?> deleteUserProfileImage(
        @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        fileService.deleteProfileImageFileByUserId(userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<String> getProfileImageUrl(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        String imageUrl = fileService.findProfileImageUrlByUserId(userId);

        if (imageUrl != null) {
            return ResponseEntity.ok(imageUrl);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile image not found");
        }
    }
}
