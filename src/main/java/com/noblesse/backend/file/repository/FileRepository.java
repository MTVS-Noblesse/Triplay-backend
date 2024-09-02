package com.noblesse.backend.file.repository;

import com.noblesse.backend.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findFilesByPostId(Long postId);
    void deleteFilesByPostId(Long postId);
    List<File> findFilesByClipId(Long clipId);
}
