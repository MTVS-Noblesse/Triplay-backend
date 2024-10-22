package com.noblesse.backend.file.dto;

import com.noblesse.backend.file.entity.File;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDTO {

    private Long fileId;
    private String fileUrl;
    private Long postId;
    private Long postPlaceId;
    private Long postImageOrder;

    public FileDTO(File file, String fileUrl) {
        this.fileId = file.getFileId();
        this.postId = file.getPostId();
        this.postPlaceId = file.getPostPlaceId();
        this.postImageOrder = file.getPostImageOrder();
        this.fileUrl = fileUrl;
    }
}
