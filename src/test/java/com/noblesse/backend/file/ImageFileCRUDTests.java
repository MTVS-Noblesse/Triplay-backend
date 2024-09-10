package com.noblesse.backend.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.noblesse.backend.file.repository.FileRepository;
import com.noblesse.backend.file.service.ImageFileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ImageFileCRUDTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ImageFileService imageFileService;

    @DisplayName("파일 단일 업로드 테스트")
    @Test
    void testUploadImageFile() throws IOException {
        // MockMultipartFile 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", // 파라미터 이름
                "test.jpg", // 파일 이름
                "image/jpeg", // 콘텐츠 타입
                "test content".getBytes() // 파일 내용
        );

        Assertions.assertDoesNotThrow(() -> {
            Blob blob = imageFileService.uploadImageFile(mockFile, "");
            System.out.println("Blob 다운로드 링크 : " + blob.signUrl(1, TimeUnit.HOURS).toString());
        });
    }

    @DisplayName("파일 단일 조회 테스트")
    @Test
    void testFindImageDownloadLink() {
        Assertions.assertDoesNotThrow(() -> {
            String mediaLink = imageFileService.findImageDownloadLink("", "test.png");
            System.out.println("Blob 다운로드 링크 : " + mediaLink);
        });
    }

    @DisplayName("파일 단일 삭제 테스트")
    @Test
    void testDeleteImageFile() {
        Assertions.assertDoesNotThrow(() -> {
            imageFileService.deleteImage("", "test.png");
        });
    }
}
