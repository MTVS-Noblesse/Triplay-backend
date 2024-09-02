package com.noblesse.backend.file.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import com.noblesse.backend.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ImageFileService {
    private final FileRepository fileRepository;

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    public ImageFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Blob uploadImageFile(MultipartFile file, String filePath) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

        InputStream content = new ByteArrayInputStream(file.getBytes());
        Blob blob = bucket.create(filePath + "/" + file.getOriginalFilename(), content, file.getContentType());

        return blob;
    }

    @Transactional
    public List<Blob> uploadImageFiles(MultipartFile[] fileList, String filePath) throws IOException {
        List<Blob> blobList = new ArrayList<>();

        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

        for (int i = 0; i < fileList.length; i++) {
            InputStream content = new ByteArrayInputStream(fileList[i].getBytes());
            // 파일 이름에 디렉토리 이름을 포함하여 저장
            Blob blob = bucket.create(filePath + "/" + fileList[i].getOriginalFilename(), content, fileList[i].getContentType());
            blobList.add(blob);
        }

        return blobList;
    }

    public String findImageDownloadLink(String directory, String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        Blob blob = bucket.get(directory + "/" + fileName);
        if(blob == null) return null;
        return blob.getMediaLink();
    }

    @Transactional
    public void deleteImage(String directory, String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        boolean isDeleted = bucket.get(directory + "/" + fileName).delete();

        if(isDeleted) System.out.println("이미지 삭제 완료 : " + directory + "/" + fileName);
        else System.out.println("이미지 삭제 오류 : " + directory + "/" + fileName);
    }

    @Transactional
    public void deleteImagesByPostId(Long postId) {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

        Iterable<Blob> blobs = bucket.list(Storage.BlobListOption.prefix("post/" + postId + "/")).getValues();
        List<Blob> blobList = StreamSupport.stream(blobs.spliterator(), false).toList();

        blobList.forEach(blob -> {
            blob.delete();
        });
        System.out.println("이미지 파일들 삭제 성공 : " + LocalDateTime.now());
    }
}
