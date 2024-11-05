package com.noblesse.backend.follow.repository;

import com.noblesse.backend.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByToUserIdAndFromUserId(@Param("toUserId") Long toUserId, @Param("fromUserId") Long fromUserId);

    @Modifying
    @Query("delete from Follow f where f.toUserId = :toUserId and f.fromUserId = :fromUserId")
    void deleteByToUserIdAndFromUserId(@Param("toUserId") Long toUserId, @Param("fromUserId") Long fromUserId);

    List<Follow> findByFromUserId(@Param("fromUserId") Long fromUserId);

    List<Follow> findByToUserId(@Param("toUserId") Long toUserId);
}
