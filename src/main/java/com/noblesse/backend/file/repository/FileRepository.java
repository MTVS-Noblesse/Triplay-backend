package com.noblesse.backend.file.repository;

import com.noblesse.backend.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findFileByFileId(Long fileId);
    File findFileByPostIdAndFileName(Long postId, String fileName);
    List<File> findFilesByPostId(Long postId);
    List<File> findFilesByClipId(Long clipId);
    void deleteFilesByPostId(Long postId);
    void deleteFileByPostIdAndFileName(Long postId, String fileName);
}
