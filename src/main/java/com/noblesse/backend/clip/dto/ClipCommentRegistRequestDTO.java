package com.noblesse.backend.clip.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClipCommentRegistRequestDTO {
    private String clipCommentContent;
    private Long userId;
    private Long clipId;
}
