package com.noblesse.backend.file.service;

import com.google.cloud.storage.Blob;
import com.noblesse.backend.file.entity.File;
import com.noblesse.backend.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileService {
    // imageService에 directory 값으로 넘길 때 경로를 /없이 바로 시작해야 합니다.
    private final ImageFileService imageFileService;
    private final FileRepository fileRepository;

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    public FileService(ImageFileService imageFileService, FileRepository fileRepository) {
        this.imageFileService = imageFileService;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public void insertImageFilesByPostId(MultipartFile[] files, Long postId) throws IOException {
        imageFileService.uploadImageFiles(files, "post/" + postId + "/");
        for(int i = 0; i < files.length; i++)
            fileRepository.save(new File(
                    "post",
                    files[i].getOriginalFilename(),
                    "gs://" + firebaseBucket + "/post/" + postId + "/" + files[i].getOriginalFilename(),
                    postId,
                    i,
                    null,
                    null));

        System.out.println("파일 추가 시간 : " + LocalDateTime.now());
    }

    @Transactional
    public String findImageDownloadLinkByPostIdAndFileName(Long postId, String fileName) {
        return imageFileService.findImageDownloadLink("post/" + postId + "/", fileName);
    }

    @Transactional
    public String findImageDownloadLinkByFileUrl(String fileUrl) {
        return imageFileService.findImageDownloadLinkByFileUrl(fileUrl);
    }

    @Transactional
    public List<String> findImageDownloadLinksByPostId(Long postId) {
        List<File> foundFiles = fileRepository.findFilesByPostId(postId);
        List<String> downloadLinks = new ArrayList<>();
        foundFiles.forEach(file -> {
            String newIamgeUrl = imageFileService.findImageDownloadLink("post/" + postId + "/", file.getFileName());
            file.setFileUrl(newIamgeUrl);
            downloadLinks.add(newIamgeUrl);
        });
        return downloadLinks;
    }

    @Transactional
    public void deleteImageFileByPostIdAndFileName(Long postId, String fileName) {
        imageFileService.deleteImage("post/" + postId + "/", fileName);
        fileRepository.deleteFileByPostIdAndFileName(postId, fileName);
    }

    @Transactional
    public void deleteImageFilesByPostId(Long postId) {
        imageFileService.deleteImagesByPostId(postId);
        fileRepository.deleteFilesByPostId(postId);
    }
}
