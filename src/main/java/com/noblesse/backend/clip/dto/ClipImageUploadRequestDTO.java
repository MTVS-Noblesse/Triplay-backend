package com.noblesse.backend.clip.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClipImageUploadRequestDTO {
    private Long tripId;
    private MultipartFile[] files;
}
