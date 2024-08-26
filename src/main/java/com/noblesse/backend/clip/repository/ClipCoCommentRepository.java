package com.noblesse.backend.clip.repository;

import com.noblesse.backend.clip.domain.ClipCoComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipCoCommentRepository extends JpaRepository<ClipCoComment, Long> {
    ClipCoComment findClipCoCommentByClipCoCommentId(Long clipCoCommentId);
    List<ClipCoComment> findClipCoCommentsByClipCommentId(Long clipCommentId);
}
