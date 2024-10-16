package com.noblesse.backend.post.api.query;

import com.noblesse.backend.post.common.dto.PostCoCommentDTO;
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
@RequestMapping(value = "/postCoComment")
@RequiredArgsConstructor
@Tag(name = "Post Query")
public class PostCoCommentQueryController {

    private final PostQueryService postQueryService;

    @Operation(summary = "포스트 대댓글 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/{postCoCommentId}")
    public ResponseEntity<PostCoCommentDTO> getPostCoCommentById(@PathVariable("postCoCommentId") Long postCoCommentId) {
        PostCoCommentDTO postCoComment = postQueryService.getPostCoCommentById(postCoCommentId);
        return ResponseEntity.ok(postCoComment);
    }

    @Operation(summary = "포스트 댓글에 따른 대댓글 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/postComment/{postCommentId}")
    public ResponseEntity<List<PostCoCommentDTO>> getPostCoCommentsByPostCommentId(@PathVariable("postCommentId") Long postCommentId) {
        List<PostCoCommentDTO> postCoComments = postQueryService.getPostCoCommentsByPostCommentId(postCommentId);
        return ResponseEntity.ok(postCoComments);
    }
}
