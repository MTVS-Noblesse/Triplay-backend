package com.noblesse.backend.post.api.query;

import com.noblesse.backend.oauth2.util.JwtUtil;
import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.post.query.application.service.PostQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
@RequiredArgsConstructor
@Tag(name = "Post Query")
public class PostQueryController {

    private final PostQueryService postQueryService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "포스트 목록 조회")
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postQueryService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "사용자가 작성한 포스트 목록 조회")
    @GetMapping("/user")
    public ResponseEntity<List<PostDTO>> getUserPosts(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        List<PostDTO> posts = postQueryService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "포스트 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("postId") Long postId) {
        PostDTO post = postQueryService.getPostById(postId);
        return ResponseEntity.ok(post);
    }
}