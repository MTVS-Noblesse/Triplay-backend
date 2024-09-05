package com.noblesse.backend.follow;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.repository.FollowRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class FollowTest {

    @Autowired
    FollowRepository followRepository;

    @BeforeEach
    void beforeEach() {
        followRepository.deleteAll();

        followRepository.save(new Follow(1L, 2L));
        followRepository.save(new Follow(2L, 1L));
        followRepository.save(new Follow(2L, 3L));
        followRepository.save(new Follow(3L, 2L));
        followRepository.save(new Follow(3L, 4L));
        followRepository.save(new Follow(3L, 1L));
        followRepository.save(new Follow(4L, 1L));
    }

    @DisplayName("follow 존재 여부 테스트")
    @Test
    void existFollowTest() {
        boolean isExist = followRepository.existsByToUserIdAndFromUserId(1L, 2L);
        Assertions.assertThat(isExist).isEqualTo(true);
    }

    @DisplayName("follow 삭제 테스트")
    @Test
    @Transactional
    void deleteFollowTest() {
        followRepository.deleteByToUserIdAndFromUserId(1L, 2L);
        boolean isExist = followRepository.existsByToUserIdAndFromUserId(1L, 2L);
        Assertions.assertThat(isExist).isEqualTo(false);
    }

    @DisplayName("특정 유저의 follower 리스트 조회 테스트")
    @Test
    void findFollowersTest() {
        List<Follow> followers = followRepository.findByFromUserId(1L);
        Assertions.assertThat(followers.size()).isEqualTo(3);
    }
    
    @DisplayName("특정 유저가 팔로우한 유저 리스트 조회 테스트")
    @Test
    void findFolloweeTest() {
        List<Follow> followees = followRepository.findByToUserId(3L);
        Assertions.assertThat(followees.size()).isEqualTo(3);
    }
}
