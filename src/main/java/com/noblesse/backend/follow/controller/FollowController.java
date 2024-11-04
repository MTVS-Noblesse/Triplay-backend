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


    @Autowired
    public FollowController(FollowService followService, JwtUtil jwtUtil, PostQueryService postQueryService) {
        this.followService = followService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{targetId}")
    public ResponseEntity<Follow> follow(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("targetId") Long targetId) {
        return ResponseEntity.ok(followService.follow(authorizationHeader, targetId));
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<?> unFollow(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("targetId") Long targetId) {
        followService.unFollow(authorizationHeader, targetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/is-following/{targetId}")
    public boolean isFollowing(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("targetId") Long targetId){
        return followService.isFollowing(authorizationHeader, targetId);
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
