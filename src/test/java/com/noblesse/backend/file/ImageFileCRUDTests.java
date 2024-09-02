package com.noblesse.backend.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.noblesse.backend.file.service.ImageFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class ImageFileCRUDTests {
    private ImageFileService imageFileService;
    private Bucket mockBucket;
    private Blob mockBlob;

    @BeforeEach
    public void setUp() {
        // Mock 객체 생성
        mockBucket = Mockito.mock(Bucket.class);
        mockBlob = Mockito.mock(Blob.class);
    }

    @DisplayName("파일 업로드 테스트")
    @Test
    public void testUploadImageFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("image", "test.png", "image/png", "test image content".getBytes());

        when(mockBucket.create(any(String.class), any(ByteArrayInputStream.class), any(String.class))).thenReturn(mockBlob);

        Blob result = firebaseStorageService.uploadImageFile(file, "uploads");
    }
}
