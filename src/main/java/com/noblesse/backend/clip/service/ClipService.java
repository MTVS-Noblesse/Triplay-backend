package com.noblesse.backend.clip.service;

import com.noblesse.backend.clip.domain.Clip;
import com.noblesse.backend.clip.dto.ClipImageUploadRequestDTO;
import com.noblesse.backend.clip.dto.ClipRegistRequestDTO;
import com.noblesse.backend.clip.repository.ClipRepository;
import com.noblesse.backend.file.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class ClipService {

    private final ClipRepository clipRepository;
    private final FileService fileService;

    public ClipService(ClipRepository clipRepository, FileService fileService) {
        this.clipRepository = clipRepository;
        this.fileService = fileService;
    }

    public Clip findClipByClipId(Long ClipId) {
        return clipRepository.findClipByClipId(ClipId);
    }

    public List<Clip> findAll() {
        return clipRepository.findAll();
    }

    @Transactional
    public Long uploadImageFiles(ClipImageUploadRequestDTO clipImageUploadRequestDTO, Long userId) throws IOException {
        Clip clip = new Clip(
                null,
                null,
                false,
                userId,
                clipImageUploadRequestDTO.getTripId()
        );
        Clip savedClip = clipRepository.save(clip);
        fileService.insertClipImageFiles(clipImageUploadRequestDTO.getFiles(), savedClip.getClipId());
        return savedClip.getClipId();
    }

    @Transactional
    public void insertClip(ClipRegistRequestDTO clipRegistRequestDTO, Long clipId) throws IOException {
        Clip foundClip = clipRepository.findClipByClipId(clipId);

        if (foundClip != null) {
            foundClip.setClipTitle(clipRegistRequestDTO.getClipTitle());
            foundClip.setOpened(clipRegistRequestDTO.getIsOpened());
            foundClip.setClipUrl("clip/" + clipId + "/");
        }
    }

    @Transactional
    public void updateClipByClipIdForExposeYN(Long ClipId) {
        Clip foundClip = findClipByClipId(ClipId);
        if(foundClip != null) {
            foundClip.setOpened(!foundClip.getOpened());
        }
    }

    @Transactional
    public void deleteClipByClipId(Long ClipId) {
        fileService.deleteClipFileByClipId(ClipId);
        clipRepository.deleteById(ClipId);
    }
}
