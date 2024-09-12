package com.noblesse.backend.post.common.dto;

import com.noblesse.backend.post.common.entity.Post;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long postId;
    private String postTitle;
    private String postContent;
    private LocalDateTime writtenDatetime;
    private LocalDateTime modifiedDatetime;
    private Boolean isOpened;
    private Long userId;
    private Long tripId;
    private Long clipId;
    // private List<PostCommentDTO> comments;

    @Nullable
    private List<String> imageUrls;

    @Nullable
    private List<String> imageUrlsToRemove;

    @Nullable
    private List<MultipartFile> newImages;
//    private MultipartFile[] newImages;

    // Create 용 생성자
    public PostDTO(String postTitle, String postContent, Boolean isOpened, Long userId, Long tripId, Long clipId) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isOpened = isOpened;
        this.userId = userId;
        this.tripId = tripId;
        this.clipId = clipId;
    }

    // Create 용 생성자
    public PostDTO(String postTitle, Long userId) {
        this.postTitle = postTitle;
        this.userId = userId;
    }

    // Update 용 생성자
    public PostDTO(Long postId, String postTitle, String postContent, Boolean isOpened, Long userId) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isOpened = isOpened;
        this.userId = userId;
    }

    // Delete 용 생성자
    public PostDTO(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public PostDTO(Long postId) {
        this.postId = postId;
    }

    // Entity로부터 DTO를 생성하는 생성자
    public PostDTO(Post post) {
        this.postId = post.getPostId();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.writtenDatetime = post.getWrittenDatetime();
        this.modifiedDatetime = post.getModifiedDatetime();
        this.isOpened = post.getIsOpened();
        this.userId = post.getUserId();
        this.tripId = post.getTripId();
        this.clipId = post.getClipId();
        this.imageUrls = post.getImageUrls(); // 이미지 URL 추가
    }
}