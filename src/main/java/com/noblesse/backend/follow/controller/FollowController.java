package com.noblesse.backend.follow.controller;

import com.noblesse.backend.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @PostMapping("/{toUserId}/{fromUserId}")
    public String follow(@PathVariable Long toUserId, @PathVariable Long fromUserId) {
        followService.follow(toUserId, fromUserId);
        return "follow success";
    }

    @DeleteMapping("/{toUserId}/{fromUserId}")
    public String unFollow(@PathVariable Long toUserId, @PathVariable Long fromUserId) {
        followService.unFollow(toUserId, fromUserId);
        return "unFollow success";
    }

    @GetMapping("/{toUserId}/{fromUserId}")
    public boolean isFollowing(@PathVariable Long toUserId, @PathVariable Long fromUserId){
        return followService.isFollowing(toUserId, fromUserId);
    }
}
