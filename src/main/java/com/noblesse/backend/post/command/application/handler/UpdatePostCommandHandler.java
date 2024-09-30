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

    private Post handleImageUpdates(Post post, PostDTO command) {
        Post updatedPost = post;

        // 현재 포스트의 이미지 URL 목록을 복사
        List<String> currentImageUrls = new ArrayList<>(post.getImageUrls());

        // 삭제할 이미지 URL 목록 (없으면 빈 리스트 사용)
        List<String> imageUrlsToRemove = command.getImageUrlsToRemove() != null ? command.getImageUrlsToRemove() : new ArrayList<>();

        // 새로 추가하거나 수정할 이미지 정보 목록 (없으면 빈 리스트 사용)
        List<Map<String, Object>> newImages = command.getNewImages() != null ? command.getNewImages() : new ArrayList<>();

        // 1. 삭제할 이미지 처리
        for (String imageUrl : imageUrlsToRemove) {
            try {
                // URL에서 파일명 추출
                String fileName = extractFileNameFromUrl(imageUrl);
                // 파일 시스템에서 이미지 파일 삭제
                fileService.deleteImageFileByPostIdAndFileName(post.getPostId(), fileName);
                // 현재 이미지 URL 목록에서 해당 URL 제거
                currentImageUrls.remove(imageUrl);
            } catch (Exception e) {
                System.out.println("이미지 삭제 중 오류 발생: " + imageUrl);
            }
        }

        // 2. 새 이미지 및 수정된 이미지 처리
        List<Map<String, Object>> imagesToUpload = new ArrayList<>();
        for (Map<String, Object> newImage : newImages) {
            MultipartFile file = (MultipartFile) newImage.get("file");
            String fileName = file.getOriginalFilename();

            // 현재 이미지 URL 목록에 같은 파일명을 가진 이미지가 있는지 확인
            boolean isExistingImage = currentImageUrls.stream()
                    .anyMatch(url -> url.contains(fileName));

            if (isExistingImage) {
                // 기존 이미지가 수정된 경우
                String existingUrl = currentImageUrls.stream()
                        .filter(url -> url.contains(fileName))
                        .findFirst()
                        .orElse(null);
                if (existingUrl != null) {
                    // 현재 URL 목록에서 기존 이미지 URL 제거
                    currentImageUrls.remove(existingUrl);
                    // 파일 시스템에서 기존 이미지 파일 삭제
                    fileService.deleteImageFileByPostIdAndFileName(post.getPostId(), fileName);
                }
            }

            // 업로드할 이미지 목록에 추가 (새 이미지 또는 수정된 이미지)
            imagesToUpload.add(newImage);
        }

        // 3. 새 이미지 및 수정된 이미지 업로드
        if (!imagesToUpload.isEmpty()) {
            try {
                // 이미지 파일들을 업로드하고 새로운 URL 목록 받기
                List<String> uploadedImageUrls = uploadNewImages(post.getPostId(), imagesToUpload);
                // 현재 URL 목록에 새로 업로드된 이미지 URL들 추가
                currentImageUrls.addAll(uploadedImageUrls);
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 4. 최종적으로 업데이트된 이미지 URL 목록으로 포스트 업데이트
        updatedPost = updatedPost.updateImageUrls(currentImageUrls);
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