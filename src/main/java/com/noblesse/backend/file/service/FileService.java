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
import java.util.ArrayList;
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
                    "post",
                    uploadedFileList.get(i).getName().substring(uploadedFileList.get(i).getName().lastIndexOf("/") + 1),
                    uploadedFileList.get(i).signUrl(1, TimeUnit.HOURS).toString(),
                    postId,
                    i,
                    null,
                    null));

        System.out.println("파일 추가 시간 : " + LocalDateTime.now());
    }

    @Transactional
    public String findImageDownloadLinkByPostIdAndFileName(Long postId, String fileName) {
        String newImageUrl = imageFileService.findImageDownloadLink("/post/" + postId, fileName);
        File foundFile = fileRepository.findFileByPostIdAndFileName(postId, fileName);
        foundFile.setFileUrl(newImageUrl);
        return newImageUrl;
    }

    @Transactional
    public List<String> findImageDownloadLinksByPostId(Long postId) {
        List<File> foundFiles = fileRepository.findFilesByPostId(postId);
        List<String> downloadLinks = new ArrayList<>();
        foundFiles.forEach(file -> {
            String newIamgeUrl = imageFileService.findImageDownloadLink("/post/" + postId + "/", file.getFileName());
            file.setFileUrl(newIamgeUrl);
            downloadLinks.add(newIamgeUrl);
        });
        return downloadLinks;
    }

    @Transactional
    public void deleteImageFileByPostId(Long postId, String fileName) {
        imageFileService.deleteImage("/post/" + postId + "/", fileName);
        fileRepository.deleteFileByPostIdAndFileName(postId, fileName);
    }

    @Transactional
    public void deleteImageFilesByPostId(Long postId) {
        imageFileService.deleteImagesByPostId(postId);
        fileRepository.deleteFilesByPostId(postId);
    }
}
