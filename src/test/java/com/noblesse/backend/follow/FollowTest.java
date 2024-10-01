package com.noblesse.backend.follow;

import com.noblesse.backend.follow.domain.Follow;
import com.noblesse.backend.follow.repository.FollowRepository;
import com.noblesse.backend.follow.service.FollowService;
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
    private FollowRepository followRepository;

    @Autowired
    private FollowService followService;

    @BeforeEach
    void beforeEach() {
        followRepository.deleteAll();

        followService.follow(1L, 2L);
        followService.follow(1L, 3L);
        followService.follow(2L, 1L);
        followService.follow(3L, 1L);
    }

    @DisplayName("follow 존재 여부 테스트")
    @Test
    void existFollowTest() {
        boolean isExistFollow = followService.isFollowing(1L, 2L);
        Assertions.assertThat(isExistFollow).isTrue();
    }

    @DisplayName("follow 삭제 테스트")
    @Test
    @Transactional
    void deleteFollowTest() {
        followService.unFollow(1L, 2L);
        Assertions.assertThat(followService.isFollowing(1L, 2L)).isFalse();
        Assertions.assertThat(followRepository.count()).isEqualTo(3);
    }

    @DisplayName("특정 유저의 follower 리스트 조회 테스트")
    @Test
    void findFollowersTest() {
        List<Follow> foundFollowList = followService.getFollowerListByUserId(1L);
        Assertions.assertThat(foundFollowList).isNotNull();
    }

}
