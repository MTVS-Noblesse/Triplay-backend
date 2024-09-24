package com.noblesse.backend.follow.controller;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.repository.FollowRepository;
import com.noblesse.backend.follow.service.FollowService;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.post.query.application.service.PostQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.noblesse.backend.oauth2.util.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final JwtUtil jwtUtil;
    private final PostQueryService postQueryService;


    @Autowired
    public FollowController(FollowService followService, JwtUtil jwtUtil, PostQueryService postQueryService) {
        this.followService = followService;
        this.jwtUtil = jwtUtil;
        this.postQueryService = postQueryService;
    }

    @PostMapping
    public ResponseEntity<Follow> follow(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("postId") Long postId) {

        String token = authorizationHeader.substring(7);
        Long myId = jwtUtil.extractUserId(token);

        PostDTO postDTO = postQueryService.getPostById(postId);
        Long targetId = postDTO.getUserId();

        return ResponseEntity.ok(followService.follow(myId, targetId));
    }

    @DeleteMapping
    public ResponseEntity<?> unFollow(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("postId") Long postId) {

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        PostDTO postDTO = postQueryService.getPostById(postId);
        Long targetId = postDTO.getUserId();

        followService.unFollow(userId, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public boolean isFollowing(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("postId") Long postId){

        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        PostDTO postDTO = postQueryService.getPostById(postId);
        Long targetId = postDTO.getUserId();

        return followService.isFollowing(userId, targetId);
    }

    @GetMapping("/follower-list")
    public ResponseEntity<List<Follow>> getFollowerListByUserId(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(followService.getFollowerListByUserId(userId));
    }

    @GetMapping("/followee-list")
    public ResponseEntity<List<Follow>> getFolloweeListByUserId(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(followService.getFolloweeListByUserId(userId));
    }
}
