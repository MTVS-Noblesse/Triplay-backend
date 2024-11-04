package com.noblesse.backend.follow.service;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.repository.FollowRepository;
import com.noblesse.backend.oauth2.service.OAuth2Service;
import com.noblesse.backend.oauth2.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public FollowService(FollowRepository followRepository, JwtUtil jwtUtil) {
        this.followRepository = followRepository;
        this.jwtUtil = jwtUtil;
    }

    public Follow follow(String authorizationHeader, Long fromUserId){
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        System.out.println(userId);
        System.out.println(fromUserId);
        return followRepository.save(new Follow(userId, fromUserId));
    }

    @Transactional
    public void unFollow(String authorizationHeader, Long fromUserId){
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        followRepository.deleteByToUserIdAndFromUserId(userId, fromUserId);
    }

    public boolean isFollowing(String authorizationHeader, Long fromUserId){
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);
        return followRepository.existsByToUserIdAndFromUserId(userId, fromUserId);
    }

    public List<Follow> getFollowerListByUserId(Long userId){
        return followRepository.findByFromUserId(userId);
    }

    public List<Follow> getFolloweeListByUserId(Long userId){
        return followRepository.findByToUserId(userId);
    }
}
