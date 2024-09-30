package com.noblesse.backend.post.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Post")
@Table(name = "post")
@ToString
public class Post {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")  // 포스트 PK code
    private Long postId;

    @Getter
    @Column(name = "post_title") // 포스트 제목
    private String postTitle;

    @Getter
    @Column(name = "post_content") // 포스트 본문(?)
    private String postContent;

    @Getter
    @Column(name = "written_datetime") // 생성 일시
    private LocalDateTime writtenDatetime;

    @Getter
    @Column(name = "modified_datetime") // 수정 일시
    private LocalDateTime modifiedDatetime;

    @Column(name = "is_opened", nullable = false) // 공개 여부
    private Boolean isOpened = true; // 기본값: true

    @Getter
    @Column(name = "user_id") // 포스트 USER_ID
    private Long userId;

    @Getter
    @Column(name = "trip_id") // 해당 여행 TRIP_CODE
    private Long tripId;

    @Getter
    @Column(name = "clip_id")
    private Long clipId;

    @Getter
    @ElementCollection
    @CollectionTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_url", length = 1024)
    private List<String> imageUrls = new ArrayList<>();

    protected Post() {
    }

    @Builder
    public Post(String postTitle, String postContent, Boolean isOpened, Long userId, Long tripId, Long clipId) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isOpened = isOpened != null ? isOpened : true; // null이면 true 사용
        this.userId = userId;
        this.tripId = tripId;
        this.clipId = clipId;
        this.writtenDatetime = LocalDateTime.now();
        this.modifiedDatetime = LocalDateTime.now();
    }

    // 새로운 생성자 추가
    public Post(Long postId, String postTitle, String postContent, LocalDateTime writtenDatetime,
                 LocalDateTime modifiedDatetime, Boolean isOpened, Long userId, Long tripId, Long clipId,
                 List<String> imageUrls) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.writtenDatetime = writtenDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.isOpened = isOpened;
        this.userId = userId;
        this.tripId = tripId;
        this.clipId = clipId;
        this.imageUrls = new ArrayList<>(imageUrls);
    }

    public Post(Long postId, String postTitle, String postContent, LocalDateTime writtenDatetime, LocalDateTime modifiedDatetime, Boolean isOpened, Long userId, Long tripId, Long clipId) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.writtenDatetime = writtenDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.isOpened = isOpened;
        this.userId = userId;
        this.tripId = tripId;
    }

    public Post updatePost(String postTitle, String postContent, Boolean isOpened) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isOpened = isOpened != null ? isOpened : this.isOpened; // null이면 기존값 유지
        this.modifiedDatetime = LocalDateTime.now();
        return this;
    }

    public Post addImageUrls(List<String> newImageUrls) {
        List<String> updatedImageUrls = new ArrayList<>(this.imageUrls);
        updatedImageUrls.addAll(newImageUrls);
        return new Post(
                this.postId, this.postTitle, this.postContent, this.writtenDatetime,
                this.modifiedDatetime, this.isOpened, this.userId, this.tripId, this.clipId,
                updatedImageUrls
        );
    }

    public Post updateImageUrls(List<String> newImageUrls) {
        return new Post(
                this.postId, this.postTitle, this.postContent, this.writtenDatetime,
                LocalDateTime.now(), this.isOpened, this.userId, this.tripId, this.clipId,
                new ArrayList<>(newImageUrls)
        );
    }

    public Post removeImageUrl(String imageUrl) {
        List<String> updatedImageUrls = new ArrayList<>(this.imageUrls);
        updatedImageUrls.remove(imageUrl);
        return new Post(
                this.postId, this.postTitle, this.postContent, this.writtenDatetime,
                LocalDateTime.now(), this.isOpened, this.userId, this.tripId, this.clipId,
                updatedImageUrls
        );
    }

    public Boolean getIsOpened() {
        return isOpened;
    }
}
