package com.noblesse.backend.follow.controller;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<Follow> follow(@RequestParam("toUserId") Long toUserId, @RequestParam("fromUserId") Long fromUserId) {
        return ResponseEntity.ok(followService.follow(toUserId, fromUserId));
    }

    @DeleteMapping
    public ResponseEntity<?> unFollow(@RequestParam("toUserId") Long toUserId, @RequestParam("fromUserId") Long fromUserId) {
        followService.unFollow(toUserId, fromUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public boolean isFollowing(@RequestParam("toUserId") Long toUserId, @RequestParam("fromUserId") Long fromUserId){
        return followService.isFollowing(toUserId, fromUserId);
    }
}
