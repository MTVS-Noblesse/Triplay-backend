package com.noblesse.backend.post.api.command;

import com.noblesse.backend.post.command.application.handler.CreatePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.DeletePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.UpdatePostCommandHandler;
import com.noblesse.backend.post.common.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
@RequiredArgsConstructor
@Tag(name = "Post Command")
public class PostCommandController {

    private final CreatePostCommandHandler createPostCommandHandler;
    private final UpdatePostCommandHandler updatePostCommandHandler;
    private final DeletePostCommandHandler deletePostCommandHandler;

    @Operation(summary = "포스트 추가 (이미지 포함)")
    @ApiResponse(
            responseCode = "201",
            description = "포스트 생성 성공"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(
            @RequestParam("postTitle") String postTitle,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "postContent", required = false) String postContent,
            @RequestParam(value = "isOpened", required = false) Boolean isOpened,
            @RequestParam(value = "tripId", required = false) Long tripId,
            @RequestParam(value = "clipId", required = false) Long clipId,
            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages
    ) {
        PostDTO command = new PostDTO(postTitle, userId);
        command.setPostContent(postContent);
        command.setIsOpened(isOpened);
        command.setTripId(tripId);
        command.setClipId(clipId);
        command.setNewImages(newImages);

        Long postId = createPostCommandHandler.handle(command);
        return ResponseEntity.ok(postId);
    }

    @Operation(summary = "포스트 수정 (이미지 포함)")
    @ApiResponse(
            responseCode = "204",
            description = "포스트 수정 성공"
    )
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestParam(value = "postTitle", required = false) String postTitle,
            @RequestParam(value = "postContent", required = false) String postContent,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "isOpened", required = false) Boolean isOpened,
            @RequestParam(value = "tripId", required = false) Long tripId,
            @RequestParam(value = "clipId", required = false) Long clipId,
            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestParam(value = "imageUrlsToRemove", required = false) List<String> imageUrlsToRemove
    ) {
        PostDTO command = new PostDTO();
        command.setPostId(postId);
        command.setPostTitle(postTitle);
        command.setPostContent(postContent);
        command.setUserId(userId);
        command.setIsOpened(isOpened);
        command.setTripId(tripId);
        command.setClipId(clipId);
        command.setNewImages(newImages);
        command.setImageUrlsToRemove(imageUrlsToRemove);

        updatePostCommandHandler.handle(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "포스트 삭제 (이미지 포함)")
    @ApiResponse(
            responseCode = "204",
            description = "포스트 삭제 성공"
    )
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("postId") Long postId,
            @RequestParam("userId") Long userId
    ) {
        PostDTO command = PostDTO.builder()
                .postId(postId)
                .userId(userId)
                .build();
        deletePostCommandHandler.handle(command);
        return ResponseEntity.noContent().build();
    }
}