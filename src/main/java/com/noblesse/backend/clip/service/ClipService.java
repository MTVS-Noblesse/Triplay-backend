package com.noblesse.backend.clip.service;

import com.noblesse.backend.clip.domain.Clip;
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

//    @Transactional
//    public void insertClip(ClipRegistRequestDTO clipRegistRequestDTO) {
//        clipRepository.save(new Clip(
//                clipRegistRequestDTO.getClipTitle(),
//                clipRegistRequestDTO.getClipUrl(),
//                clipRegistRequestDTO.getIsOpened(),
//                clipRegistRequestDTO.getUserId(),
//                clipRegistRequestDTO.getTripId()
//        ));
//    }

    @Transactional
    public void insertClip(ClipRegistRequestDTO clipRegistRequestDTO) throws IOException {
        try {
            Clip clip = new Clip(
                    clipRegistRequestDTO.getClipTitle(),
                    null, // 임시로 null 설정
                    clipRegistRequestDTO.getIsOpened(),
                    clipRegistRequestDTO.getUserId(),
                    clipRegistRequestDTO.getTripId()
            );

            Clip savedClip = clipRepository.save(clip);

            // 저장된 후에 clip_url 업데이트
            String clipUrl = "clip/" + savedClip.getClipId() + "/" + clipRegistRequestDTO.getFile().getOriginalFilename();
            savedClip.setClipUrl(clipUrl);
            clipRepository.save(savedClip);

            fileService.insertClipFile(clipRegistRequestDTO.getFile(), savedClip.getClipId(), savedClip.getClipTitle());
        } catch (IOException e) {
            // 로그 기록
            System.err.println("클립 파일 저장 중 오류 발생: " + e.getMessage());
            // 예외를 다시 던져서 상위 레벨에서 처리할 수 있게 함
            throw new IOException("클립 파일 저장 중 오류가 발생했습니다.", e);
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
