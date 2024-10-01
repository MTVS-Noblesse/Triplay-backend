package com.noblesse.backend.post.api.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noblesse.backend.oauth2.util.JwtUtil;
import com.noblesse.backend.post.command.application.handler.CreatePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.DeletePostCommandHandler;
import com.noblesse.backend.post.command.application.handler.UpdatePostCommandHandler;
import com.noblesse.backend.post.common.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/post")
@RequiredArgsConstructor
@Tag(name = "Post Command")
public class PostCommandController {

    private final CreatePostCommandHandler createPostCommandHandler;
    private final UpdatePostCommandHandler updatePostCommandHandler;
    private final DeletePostCommandHandler deletePostCommandHandler;
    private final JwtUtil jwtUtil;

    @Operation(summary = "포스트 추가 (이미지 포함)")
    @ApiResponse(
            responseCode = "201",
            description = "포스트 생성 성공"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("postTitle") String postTitle,
            @RequestParam(value = "isOpened", required = false) Boolean isOpened,
            @RequestParam(value = "tripId", required = false) Long tripId,
            @RequestParam(value = "clipId", required = false) Long clipId,
            @RequestParam(value = "newImages", required = false) String newImagesJson, // 문자열로 받음
            @RequestParam(value = "files", required = false) MultipartFile[] files // 파일 리스트로 받음
    ) throws JsonProcessingException {
        String token = authorizationHeader.substring(7); // 앞의 "Bearer " 제거
        Long userId = jwtUtil.extractUserId(token);

        // JSON 문자열을 List<Map<String, Object>>로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        // 모든 정수를 Long으로 변환하도록 설정
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        List<Map<String, Object>> newImageInfos = objectMapper.readValue(newImagesJson, List.class);

        // newImages 리스트를 ArrayList로 초기화
        List<Map<String, Object>> newImages = new ArrayList<>();

        // 파일과 JSON 데이터 매칭
        for (int i = 0; i < newImageInfos.size(); i++) {
            Map<String, Object> newImageInfo = newImageInfos.get(i);
            if (i < files.length) {
                newImageInfo.put("file", files[i]);
            }
            newImages.add(newImageInfo);
        }

        PostDTO command = new PostDTO(postTitle, userId);
        command.setIsOpened(isOpened);
        command.setTripId(tripId);
        command.setClipId(clipId);
        command.setNewImages(newImages);

        Long postId = createPostCommandHandler.handle(command);
        return ResponseEntity.ok(postId);
    }

    @Operation(summary = "포스트 수정 (이미지 포함)")
    @ApiResponse(
            responseCode = "204",
            description = "포스트 수정 성공"
    )
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updatePost(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long postId,
            @RequestParam(value = "postTitle", required = false) String postTitle,
            @RequestParam(value = "postContent", required = false) String postContent,
            @RequestParam(value = "isOpened", required = false) Boolean isOpened,
            @RequestParam(value = "tripId", required = false) Long tripId,
            @RequestParam(value = "clipId", required = false) Long clipId,
            @RequestParam(value = "newImages", required = false) String newImagesJson,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "imageUrlsToRemove", required = false) List<String> imageUrlsToRemove
    ) throws JsonProcessingException {
        // 1. 인증 토큰에서 사용자 ID 추출
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Long userId = jwtUtil.extractUserId(token);

        // 2. JSON 문자열로 받은 newImages 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        List<Map<String, Object>> newImageInfos = objectMapper.readValue(newImagesJson, List.class);

        // 3. 파싱된 이미지 정보와 실제 파일 매칭
        List<Map<String, Object>> newImages = new ArrayList<>();
        for (int i = 0; i < newImageInfos.size(); i++) {
            Map<String, Object> newImageInfo = newImageInfos.get(i);
            if (i < files.length) {
                // 이미지 정보에 실제 파일 객체 추가
                newImageInfo.put("file", files[i]);
            }
            newImages.add(newImageInfo);
        }

        // 4. PostDTO 객체 생성 및 데이터 설정
        PostDTO command = new PostDTO();
        command.setPostId(postId);
        command.setPostTitle(postTitle);
        command.setPostContent(postContent);
        command.setUserId(userId);
        command.setIsOpened(isOpened);
        command.setTripId(tripId);
        command.setClipId(clipId);
        command.setNewImages(newImages);
        command.setImageUrlsToRemove(imageUrlsToRemove);

        // 5. UpdatePostCommandHandler를 통해 포스트 업데이트 처리
        updatePostCommandHandler.handle(command);

        // 6. 응답 반환 (204 No Content)
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "포스트 삭제 (이미지 포함)")
    @ApiResponse(
            responseCode = "204",
            description = "포스트 삭제 성공"
    )
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("postId") Long postId
    ) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        PostDTO command = PostDTO.builder()
                .postId(postId)
                .userId(userId)
                .build();
        deletePostCommandHandler.handle(command);
        return ResponseEntity.noContent().build();
    }
}