package com.noblesse.backend.clip.service;

import com.noblesse.backend.clip.domain.ClipComment;
import com.noblesse.backend.clip.dto.ClipCommentRegistRequestDTO;
import com.noblesse.backend.clip.repository.ClipCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClipCommentService {
    private final ClipCommentRepository clipCommentRepository;

    public ClipCommentService(ClipCommentRepository clipCommentRepository) {
        this.clipCommentRepository = clipCommentRepository;
    }

    public ClipComment findClipCommentByClipCommentId(Long clipCommentId) {
        return clipCommentRepository.findClipCommentByClipCommentId(clipCommentId);
    }

    public List<ClipComment> findAllClipCommentByClipId(Long clipId) {
        return clipCommentRepository.findClipCommentsByClipId(clipId);
    }

    public List<ClipComment> findAll() {
        return clipCommentRepository.findAll();
    }

    public void registClipComment(ClipCommentRegistRequestDTO clipCommentRegistRequestDTO) {
        clipCommentRepository.save(new ClipComment(
                clipCommentRegistRequestDTO.getClipCommentContent(),
                clipCommentRegistRequestDTO.getUserId(),
                clipCommentRegistRequestDTO.getClipId()
        ));
    }

    @Transactional
    public void updateClipComment(Long clipCommentId, String newContent) {
        ClipComment clipComment = findClipCommentByClipCommentId(clipCommentId);
        if (clipComment != null) {
            clipComment.setClipCommentContent(newContent);
        }
    }

    @Transactional
    public void deleteClipByClipId(Long ClipId) {
        clipCommentRepository.deleteById(ClipId);
    }
}
