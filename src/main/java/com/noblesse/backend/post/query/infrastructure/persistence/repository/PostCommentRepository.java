package com.noblesse.backend.post.query.infrastructure.persistence.repository;

import com.noblesse.backend.post.common.dto.PostCommentDTO;
import com.noblesse.backend.post.common.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findByUserId(Long userId);

    @Query("SELECT new com.noblesse.backend.post.common.dto.PostCommentDTO(" +
            "pc.postCommentId, pc.postCommentContent, pc.writtenDatetime, pc.modifiedDatetime, pc.userId, pc.postId," +
            "u.userName, f.fileUrl) " +
            "FROM PostComment pc " +
            "LEFT JOIN OAuthUser u ON pc.userId = u.id " +
            "LEFT JOIN File f ON u.profileId = f.fileId " +
            "WHERE pc.postId = :postId")
    List<PostCommentDTO> findByPostId(Long postId);
}
