package com.noblesse.backend.follow.service;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    FollowRepository followRepository;

    public void follow(Long toUserId, Long fromUserId){
        followRepository.save(new Follow(toUserId, fromUserId));
    }

    public void unFollow(Long toUserId, Long fromUserId){
        followRepository.deleteByToUserIdAndFromUserId(toUserId, fromUserId);
    }

    public boolean isFollowing(Long toUserId, Long fromUserId){
        return followRepository.existsByToUserIdAndFromUserId(toUserId, fromUserId);
    }
}
