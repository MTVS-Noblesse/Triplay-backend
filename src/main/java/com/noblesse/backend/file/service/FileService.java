package com.noblesse.backend.file.service;

import com.google.cloud.storage.Blob;
import com.noblesse.backend.file.entity.File;
import com.noblesse.backend.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileService {
    private final ImageFileService imageFileService;
    private final FileRepository fileRepository;

    public FileService(ImageFileService imageFileService, FileRepository fileRepository) {
        this.imageFileService = imageFileService;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void insertImageFilesByPostId(MultipartFile[] files, Long postId) throws IOException {
        List<Blob> uploadedFileList = imageFileService.uploadImageFiles(files, "/post/" + postId);
        for(int i = 0; i < uploadedFileList.size(); i++)
            fileRepository.save(new File(
                    "image",
                    uploadedFileList.get(i).getName().substring(uploadedFileList.get(i).getName().lastIndexOf("/") + 1),
                    uploadedFileList.get(i).signUrl(1, TimeUnit.HOURS).toString(),
                    postId,
                    i,
                    null,
                    null));

        System.out.println("파일 추가 시간 : " + LocalDateTime.now());
    }

    public void deleteImageFilesBy(Long postId) {
        imageFileService.deleteImagesByPostId(postId);
        fileRepository.deleteFilesByPostId(postId);
    }
}
