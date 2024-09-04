package com.noblesse.backend.post.api.query;

import com.noblesse.backend.post.common.dto.PostCommentDTO;
import com.noblesse.backend.post.common.entity.Post;
import com.noblesse.backend.post.query.application.service.PostQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/postComment")
@RequiredArgsConstructor
@Tag(name = "Post Query")
public class PostCommentQueryController {

    private final PostQueryService postQueryService;

    @Operation(summary = "포스트 댓글 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{postCommentId}")
    public ResponseEntity<PostCommentDTO> getPostCommentById(@PathVariable("postCommentId") Long postCommentId) {
        PostCommentDTO postComment = postQueryService.getPostCommentById(postCommentId);
        return ResponseEntity.ok(postComment);
    }

    @Operation(summary = "포스트에 따른 댓글 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostCommentDTO>> getPostCommentsByPostId(@PathVariable("postId") Long postId) {
        List<PostCommentDTO> postComments = postQueryService.getPostCommentsByPostId(postId);
        return ResponseEntity.ok(postComments);
    }
}
