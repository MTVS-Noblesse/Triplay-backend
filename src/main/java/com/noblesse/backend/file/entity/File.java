package com.noblesse.backend.file.entity;

import jakarta.persistence.*;

@Entity(name = "File")
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long fileId;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "POST_ORDER")
    private Integer postOrder;

    @Column(name = "CLIP_ID")
    private Long clipId;

    @Column(name = "CLIP_ORDER")
    private Integer clipOrder;

    public File() {
    }

    public File(String fileType, String fileName, String fileUrl, Long postId, Integer postOrder, Long clipId, Integer clipOrder) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.postId = postId;
        this.postOrder = postOrder;
        this.clipId = clipId;
        this.clipOrder = clipOrder;
    }

    public Integer getPostOrder() {
        return postOrder;
    }

    public Integer getClipOrder() {
        return clipOrder;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getClipId() {
        return clipId;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setClipId(Long clipId) {
        this.clipId = clipId;
    }
}
