package com.noblesse.backend.post.api.command;

import com.noblesse.backend.post.command.application.handler.CreatePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.DeletePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.UpdatePostCommandHandler;
import com.noblesse.backend.post.common.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @ModelAttribute PostDTO command
    ) {
        Long postId = createPostCommandHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @Operation(summary = "포스트 수정 (이미지 포함)")
    @ApiResponse(
            responseCode = "204",
            description = "포스트 수정 성공"
    )
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @PathVariable("postId") Long postId,
            @ModelAttribute PostDTO command
    ) {
        command.setPostId(postId);
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
        PostDTO command = new PostDTO();
        command.setPostId(postId);
        command.setUserId(userId);
        deletePostCommandHandler.handle(command);
        return ResponseEntity.noContent().build();
    }
}