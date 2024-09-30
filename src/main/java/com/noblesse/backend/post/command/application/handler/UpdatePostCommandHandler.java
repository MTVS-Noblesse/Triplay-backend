package com.noblesse.backend.post.command.application.handler;

import com.noblesse.backend.file.service.FileService;
import com.noblesse.backend.post.command.domain.publisher.PostEventPublisher;
import com.noblesse.backend.post.command.domain.service.PostDomainService;
import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.post.common.entity.Post;
import com.noblesse.backend.post.common.exception.PostNotFoundException;
import com.noblesse.backend.post.query.infrastructure.persistence.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UpdatePostCommandHandler {

    private final PostRepository postRepository;
    private final PostDomainService postDomainService;
    private final PostEventPublisher postEventPublisher;
    private final FileService fileService;

    @Transactional
    public void handle(PostDTO command) {
        Post post = postRepository.findById(command.getPostId())
                .orElseThrow(() -> new PostNotFoundException(command.getPostId()));

        postDomainService.validatePostUpdate(post, command.getUserId());

        Post updatePost = updatePostFields(post, command);

        // 이미지 처리
        updatePost = handleImageUpdates(updatePost, command);

        Post savedPost = postRepository.save(updatePost);
        postEventPublisher.publishPostUpdatedEvent(savedPost);
    }

    private Post updatePostFields(Post post, PostDTO command) {
        boolean isUpdated = false;
        String newTitle = post.getPostTitle();
        String newContent = post.getPostContent();
        Boolean newIsOpened = post.getIsOpened();
        Long newTripId = post.getTripId();
        Long newClipId = post.getClipId();

        if (command.getPostTitle() != null && !command.getPostTitle().equals(post.getPostTitle())) {
            newTitle = command.getPostTitle();
            isUpdated = true;
        }
        if (command.getPostContent() != null && !command.getPostContent().equals(post.getPostContent())) {
            newContent = command.getPostContent();
            isUpdated = true;
        }
        if (command.getIsOpened() != null && !command.getIsOpened().equals(post.getIsOpened())) {
            newIsOpened = command.getIsOpened();
            isUpdated = true;
        }
        if (command.getTripId() != null && !command.getTripId().equals(post.getTripId())) {
            newTripId = command.getTripId();
            isUpdated = true;
        }
        if (command.getClipId() != null && !command.getClipId().equals(post.getClipId())) {
            newClipId = command.getClipId();
            isUpdated = true;
        }

        if (isUpdated) {
            return new Post(
                    post.getPostId(),
                    newTitle,
                    newContent,
                    post.getWrittenDatetime(),
                    LocalDateTime.now(),
                    newIsOpened,
                    post.getUserId(),
                    newTripId,
                    newClipId,
                    new ArrayList<>(post.getImageUrls())
            );
        }

        return post; // 변경사항이 없으면 원본 Post 객체 반환
    }

    //    private void handleImageUpdates(Post post, PostDTO command) {
//        if (command.getImageUrlsToRemove() != null && !command.getImageUrlsToRemove().isEmpty()) {
//            System.out.println("삭제 요청된 이미지 URL: " + command.getImageUrlsToRemove());
//            for (String imageUrl : command.getImageUrlsToRemove()) {
//                try {
//                    String fileName = extractFileNameFromUrl(imageUrl);
//                    System.out.println("삭제 시도 중인 파일 이름: " + fileName);
//                    fileService.deleteImageFileByPostIdAndFileName(post.getPostId(), fileName);
//                } catch (Exception e) {
//                    System.out.println("이미지 삭제 중 오류 발생: " + imageUrl);
//                    e.printStackTrace();
//                }
//            }
//            post.removeImageUrls(command.getImageUrlsToRemove());
//        }
//
//        if (command.getNewImages() != null && !command.getNewImages().isEmpty()) {
//            try {
//                List<String> newImageUrls = uploadNewImages(post.getPostId(), command.getNewImages());
//                post.addImageUrls(newImageUrls);
//            } catch (IOException e) {
//                throw new RuntimeException("새 이미지 업로드 중 오류가 발생했습니다.", e);
//            }
//        }
//    }
    private Post handleImageUpdates(Post post, PostDTO command) {
        Post updatedPost = post;

        if (command.getImageUrlsToRemove() != null && !command.getImageUrlsToRemove().isEmpty()) {
            for (String imageUrl : command.getImageUrlsToRemove()) {
                try {
                    String fileName = extractFileNameFromUrl(imageUrl);
                    fileService.deleteImageFileByPostIdAndFileName(post.getPostId(), fileName);
                    updatedPost = updatedPost.removeImageUrl(imageUrl);
                } catch (Exception e) {
                    System.out.println("이미지 삭제 중 오류 발생: " + imageUrl);
                }
            }
        }

        if (command.getNewImages() != null && !command.getNewImages().isEmpty()) {
            try {
                List<String> newImageUrls = uploadNewImages(post.getPostId(), command.getNewImages());
                updatedPost = updatedPost.addImageUrls(newImageUrls);
            } catch (IOException e) {
                throw new RuntimeException("새 이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        return updatedPost;
    }

    private List<String> uploadNewImages(Long postId, List<Map<String, Object>> images) throws IOException {
        fileService.insertPostImageFilesByPostId(images, postId);
        return fileService.findImageDownloadLinksByPostId(postId);
    }

    private String extractFileNameFromUrl(String url) {
        try {
            return url.substring(url.lastIndexOf('/') + 1, url.indexOf('?'));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid image URL format: " + url, e);
        }
    }
}