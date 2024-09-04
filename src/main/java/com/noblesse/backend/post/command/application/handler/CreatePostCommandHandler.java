package com.noblesse.backend.post.command.application.handler;

import com.noblesse.backend.file.service.FileService;
import com.noblesse.backend.post.command.domain.publisher.PostEventPublisher;
import com.noblesse.backend.post.command.domain.service.PostDomainService;
import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.post.common.entity.Post;
import com.noblesse.backend.post.query.infrastructure.persistence.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreatePostCommandHandler {

    private final PostRepository postRepository;
    private final PostDomainService postDomainService;
    private final PostEventPublisher postEventPublisher;
    private final FileService fileService;

    @Transactional
    public Long handle(PostDTO command) {
        // 1. 입력값 유효성 검증
        validateCreatePostCommand(command);

        // 2. Post 엔티티 생성
        Post newPost = createPostFromCommand(command);

        // 3. 비즈니스 로직 적용
        postDomainService.validatePostContent(newPost);

        // 4. 저장소에 저장
        Post savedPost = postRepository.save(newPost);

        // 5. 이미지 업로드 처리
        if (command.getImages() != null && !command.getImages().isEmpty()) {
            try {
                handleImageUpload(savedPost.getPostId(), command.getImages());
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 6. 이벤트 발행
        postEventPublisher.publishPostCreatedEvent(savedPost);

        return savedPost.getPostId();
    }

    private void validateCreatePostCommand(PostDTO command) {
        if (command.getPostTitle() == null || command.getPostTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("포스트 제목은 공백일 수 없어요...");
        }

        if (command.getPostContent() == null || command.getPostContent().trim().isEmpty()) {
            throw new IllegalArgumentException("포스트 본문은 공백일 수 없어요...");
        }

        if (command.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 지정되어야 해요...");
        }
    }

    private Post createPostFromCommand(PostDTO command) {
        return Post.builder()
                .postTitle(command.getPostTitle())
                .postContent(command.getPostContent())
                .writtenDatetime(LocalDateTime.now())
                .modifiedDatetime(LocalDateTime.now())
                .isOpened(command.getIsOpened())
                .userId(command.getUserId())
                .tripId(command.getTripId())
                .clipId(command.getClipId())
                .build();
    }

    public void handleImageUpload(Long postId, List<MultipartFile> images) throws IOException {
        fileService.insertImageFilesByPostId(images.toArray(new MultipartFile[0]), postId);
    }
}
