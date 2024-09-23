package com.noblesse.backend.notice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class NewNoticeDTO {
    private Long id;
    private String title;
    private String content;

    public NewNoticeDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
