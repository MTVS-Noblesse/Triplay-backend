package com.noblesse.backend.file.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "File")
@Table(name = "file")
@Getter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long fileId;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_NAME")
    private String fileName;

    // 해당 fileUrl은 파일이 저장된 위치 URL로 실제 인터넷에서 접속되는 URL과 다름
    // 따로 인터넷에서 접근 가능한 주소를 발급하는 service 코드를 통해 얻어야 함
    // 이렇게 저장하는 이유는 인터넷에서 접속하는 URL은 엑세스 토큰을 발급 받아 재조합된 URL이여서
    // 고정되지 않고 계속 URL이 달라지기 때문임.
    @Column(name = "FILE_URL", length = 1024)
    private String fileUrl;

    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "POST_PLACE_ID")
    private Long postPlaceId;

    @Column(name = "POST_IMAGE_ORDER")
    private Long postImageOrder;

    @Column(name = "CLIP_ID")
    private Long clipId;

    @Column(name = "CLIP_ORDER")
    private Long clipOrder;

    public File() {
    }

    public File(String fileType, String fileName, String fileUrl, Long postId, Long postPlaceId, Long postImageOrder, Long clipId, Long clipOrder) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.postId = postId;
        this.postPlaceId = postPlaceId;
        this.postImageOrder = postImageOrder;
        this.clipId = clipId;
        this.clipOrder = clipOrder;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setPostPlaceId(Long postPlaceId) {
        this.postPlaceId = postPlaceId;
    }

    public void setPostImageOrder(Long postImageOrder) {
        this.postImageOrder = postImageOrder;
    }

    public void setClipId(Long clipId) {
        this.clipId = clipId;
    }

    public void setClipOrder(Long clipOrder) {
        this.clipOrder = clipOrder;
    }
}