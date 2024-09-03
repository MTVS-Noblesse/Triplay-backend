package com.noblesse.backend.file.controller;

import com.noblesse.backend.file.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file/image")
public class ImageFileController {
    private final FileService fileService;

    public ImageFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/{postId}/new")
    @ResponseBody
    public ResponseEntity<?> registNewImagesByPostId(
            @RequestParam MultipartFile[] files,
            @PathVariable Long postId) throws IOException {

        fileService.insertImageFilesByPostId(files, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<?> findImageDownloadLinksByPostId(
            @PathVariable Long postId) throws IOException {

        return ResponseEntity.ok(fileService.findImageDownloadLinksByPostId(postId));
    }

    @GetMapping("/{postId}/{fileName}")
    @ResponseBody
    public ResponseEntity<?> findImageDownloadLinkByPostIdAndFileName(
            @PathVariable Long postId,
            @PathVariable String fileName) throws IOException {

        return ResponseEntity.ok(fileService.findImageDownloadLinkByPostIdAndFileName(postId, fileName));
    }

    @DeleteMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<?> deleteImageFileByPostId(@PathVariable Long postId) throws IOException {
        fileService.deleteImageFilesByPostId(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/{fileName}")
    @ResponseBody
    public ResponseEntity<?> deleteImageFileByPostIdAndFileName(@PathVariable Long postId, @PathVariable String fileName) throws IOException {
        fileService.deleteImageFileByPostIdAndFileName(postId, fileName);
        return ResponseEntity.ok().build();
    }
}
