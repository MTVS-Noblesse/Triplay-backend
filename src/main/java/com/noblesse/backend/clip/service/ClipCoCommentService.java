package com.noblesse.backend.clip.service;

import com.noblesse.backend.clip.domain.ClipCoComment;
import com.noblesse.backend.clip.dto.ClipCoCommentRegistRequestDTO;
import com.noblesse.backend.clip.repository.ClipCoCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClipCoCommentService {
    private final ClipCoCommentRepository clipCoCommentRepository;

    public ClipCoCommentService(ClipCoCommentRepository clipCoCommentRepository) {
        this.clipCoCommentRepository = clipCoCommentRepository;
    }

    public ClipCoComment findClipCoCommentByClipCoCommentId(Long clipCoCommentId) {
        return clipCoCommentRepository.findClipCoCommentByClipCoCommentId(clipCoCommentId);
    }

    public List<ClipCoComment> findAllClipCoCOmmentByClipCommentId(Long clipCommentId) {
        return clipCoCommentRepository.findClipCoCommentsByClipCommentId(clipCommentId);
    }

    public List<ClipCoComment> findAll() {
        return clipCoCommentRepository.findAll();
    }

    public void registClipCoComment(ClipCoCommentRegistRequestDTO clipCoCommentRegistRequestDTO) {
        clipCoCommentRepository.save(new ClipCoComment(
                clipCoCommentRegistRequestDTO.getClipCoCommentContent(),
                clipCoCommentRegistRequestDTO.getUserId(),
                clipCoCommentRegistRequestDTO.getClipCommentId()
        ));
    }

    public void updateClipCoCommentByClipCoCommentId(Long clipCoCommentId, String clipCoCommentContent) {
        ClipCoComment clipCoComment = findClipCoCommentByClipCoCommentId(clipCoCommentId);
        if (clipCoComment != null) {
            clipCoComment.setClipCoCommentContent(clipCoCommentContent);
        }
    }

    public void deleteClipCoCommentByClipCoCommentId(Long clipCoCommentId) {
        clipCoCommentRepository.deleteById(clipCoCommentId);
    }
}
