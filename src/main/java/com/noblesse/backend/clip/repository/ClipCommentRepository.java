package com.noblesse.backend.clip.repository;

import com.noblesse.backend.clip.domain.ClipComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipCommentRepository extends JpaRepository<ClipComment, Long> {
    ClipComment findClipCommentByClipCommentId(Long clipCommentId);
    List<ClipComment> findClipCommentsByClipId(Long clipId);
}
