package com.noblesse.backend.clip.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClipRegistRequestDTO {
    private String clipTitle;
    private String clipUrl;
    private Boolean isOpened;
    private Long userId;
    private Long tripId;
}
