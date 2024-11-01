package com.noblesse.backend.clip.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClipRegistRequestDTO {
    private Long clipId;
    private String clipTitle;
    private String clipUrl;
    private Boolean isOpened;
    private Long userId;
    private Long tripId;
    private MultipartFile file;
}
