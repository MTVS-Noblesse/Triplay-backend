package com.noblesse.backend.follow.repository;

import com.noblesse.backend.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

//    @Query("SELECT CASE WHEN COUNT(f) > 0 WHEN TRUE ELSE FALSE END FROM Follow f WHERE f.followerId = :follower_id AND f.followeeId = :followee_id")
    boolean existsByToUserIdAndFromUserId(@Param("to_user_id") Long toUserId, @Param("from_user_id") Long fromUserId);

    void deleteByToUserIdAndFromUserId(@Param("to_user_id") Long toUserId, @Param("from_user_id") Long fromUserId);

//    @Query("SELECT f FROM Follow f WHERE f.followeeId = :userId")
    List<Follow> findByFromUserId(@Param("user_id") Long userId);

    List<Follow> findByToUserId(@Param("user_id") Long userId);
}
