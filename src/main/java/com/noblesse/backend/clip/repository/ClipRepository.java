package com.noblesse.backend.clip.repository;

import com.noblesse.backend.clip.domain.Clip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipRepository extends JpaRepository<Clip, Long> {
    Clip findClipByClipId(Long clipId);
    List<Clip> findByUserId(Long userId);
}
