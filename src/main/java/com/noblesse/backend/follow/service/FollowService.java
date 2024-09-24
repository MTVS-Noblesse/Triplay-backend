package com.noblesse.backend.follow.service;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.repository.FollowRepository;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public Follow follow(Long toUserId, Long fromUserId){
        return followRepository.save(new Follow(toUserId, fromUserId));
    }

    public void unFollow(Long toUserId, Long fromUserId){
        followRepository.deleteByToUserIdAndFromUserId(toUserId, fromUserId);
    }

    public boolean isFollowing(Long toUserId, Long fromUserId){
        return followRepository.existsByToUserIdAndFromUserId(toUserId, fromUserId);
    }

    public List<Follow> getFollowerListByUserId(Long userId){
        return followRepository.findByFromUserId(userId);
    }

    public List<Follow> getFolloweeListByUserId(Long userId){
        return followRepository.findByToUserId(userId);
    }
}
