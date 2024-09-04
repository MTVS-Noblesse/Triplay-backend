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
import java.util.List;

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
        post.updatePost(command.getPostTitle(), command.getPostContent(), command.getIsOpened());

        // 기존 이미지 삭제
        fileService.deleteImageFilesByPostId(post.getPostId());

        // 새 이미지 업로드
        if (command.getImages() != null && !command.getImages().isEmpty()) {
            try {
                uploadNewImages(post.getPostId(), command.getImages());
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        Post updatedPost = postRepository.save(post);
        postEventPublisher.publishPostUpdatedEvent(updatedPost);
    }

    private void uploadNewImages(Long postId, List<MultipartFile> images) throws IOException {
        fileService.insertImageFilesByPostId(images.toArray(new MultipartFile[0]), postId);
    }
}
