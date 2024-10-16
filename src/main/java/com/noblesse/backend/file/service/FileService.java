package com.noblesse.backend.file.service;

import com.noblesse.backend.file.entity.File;
import com.noblesse.backend.file.repository.FileRepository;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class FileService {
    // imageService에 directory 값으로 넘길 때 경로를 /없이 바로 시작해야 합니다.
    private final ImageFileService imageFileService;
    private final FileRepository fileRepository;
    private final OAuthRepository oAuthRepository;

    public FileService(ImageFileService imageFileService, FileRepository fileRepository, OAuthRepository oAuthRepository) {
        this.imageFileService = imageFileService;
        this.fileRepository = fileRepository;
        this.oAuthRepository = oAuthRepository;
    }

    @Transactional
    public void insertPostImageFilesByPostId(List<Map<String, Object>> files, Long postId) throws IOException {

        for (Map<String, Object> fileMap : files) {
            imageFileService.uploadImageFile((MultipartFile) fileMap.get("file"), "post/" + postId + "/");
            fileRepository.save(new File(
                    "post",
                    ((MultipartFile) fileMap.get("file")).getOriginalFilename(),
                    "post/" + postId + "/" + ((MultipartFile) fileMap.get("file")).getOriginalFilename(),
                    postId,
                    (Long) fileMap.get("placeId"),
                    (Long) fileMap.get("placeImageOrder"),
                    null,
                    null));
        }

        System.out.println("파일 추가 시간 : " + LocalDateTime.now());
    }

    @Transactional
    public void insertProfileImageFileByUserId(MultipartFile file, Long userId) throws IOException {
        OAuthUser foundUser = oAuthRepository.findById(userId).orElse(null);
        if (foundUser != null) {
            String originalFileName = file.getOriginalFilename();
            imageFileService.uploadImageFile(file, "profile/");
            File savedfile = fileRepository.save(new File(
                    "profile",
                    userId.toString(),
                    "profile/" + userId.toString() + originalFileName.substring(originalFileName.lastIndexOf(".") + 1),
                    null,
                    null,
                    null,
                    null,
                    null));
            foundUser.setProfileId(savedfile.getFileId());

            System.out.println("파일 추가 시간 : " + LocalDateTime.now());
        }
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
            String newImageUrl = imageFileService.findImageDownloadLinkByFileUrl(file.getFileUrl());
            downloadLinks.add(newImageUrl);
        });
        return downloadLinks;
    }

    @Transactional
    public String findProfileImageUrlByUserId(Long userId) {
        Optional<OAuthUser> userOptional = oAuthRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        OAuthUser user = userOptional.get();
        Long profileFileId = user.getProfileId();

        if (profileFileId == null) {
            return null;
        }

        File profileImageFile = fileRepository.findFileByFileId(profileFileId);
        String newImageUrl = imageFileService.findImageDownloadLink("profile/" + userId + "/", profileImageFile.getFileName());
        profileImageFile.setFileUrl(newImageUrl);

        return newImageUrl;
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

    @Transactional
    public void deleteProfileImageFileByUserId(Long userId) {
        OAuthUser foundUser = oAuthRepository.findById(userId).orElse(null);
        if(foundUser != null) {
            imageFileService.deleteProfileImageByUserId(userId);
            fileRepository.deleteById(foundUser.getProfileId());
        }
    }
}
