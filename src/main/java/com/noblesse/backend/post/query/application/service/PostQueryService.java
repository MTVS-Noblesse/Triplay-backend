package com.noblesse.backend.post.query.application.service;

import com.noblesse.backend.file.dto.FileDTO;
import com.noblesse.backend.file.service.ImageFileService;
import com.noblesse.backend.post.common.dto.PostCoCommentDTO;
import com.noblesse.backend.post.common.dto.PostCommentDTO;
import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.post.common.dto.PostReportDTO;
import com.noblesse.backend.post.common.entity.*;
import com.noblesse.backend.post.common.exception.PostCoCommentNotFoundException;
import com.noblesse.backend.post.common.exception.PostCommentNotFoundException;
import com.noblesse.backend.post.common.exception.PostReportNotFoundException;
import com.noblesse.backend.post.query.infrastructure.persistence.repository.*;
import com.noblesse.backend.post.query.mapper.PostMapper;
import com.noblesse.backend.trip.dto.PlaceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCoCommentRepository postCoCommentRepository;
    private final PostReportRepository postReportRepository;

    private final PostMapper postMapper;

    private final ImageFileService imageFileService;

    /**
     * ### PostDTO ###
     */
    /** 포스트 고유 ID로 포스팅 검색하는 메서드 */
    public PostDTO getPostById(Long postId) {
        PostDTO post = postMapper.getPostByPostId(postId);

        post.setProfileImageUrl(imageFileService.findImageDownloadLinkByFileUrl(post.getProfileImageUrl()));

        // Trip에 속한 장소 정보 추가
        List<PlaceDTO> places = postMapper.getPlacesByTripId(post.getTripId());
        post.setPlaces(places);

        for (PlaceDTO place : places) {
            // Place에 연결된 이미지 정보 추가
            List<FileDTO> images = postMapper.getImagesByPlaceId(place.getPlaceId()); // 각 Place에 대한 이미지 조회

            List<FileDTO> transformedImages = images.stream()
                    .map(image -> {
                        String signedUrl = imageFileService.findImageDownloadLinkByFileUrl(image.getFileUrl());
                        image.setFileUrl(signedUrl); // 변환된 URL을 설정
                        return image;
                    })
                    .filter(image -> image.getFileUrl() != null) // 변환이 실패한 URL은 제외
                    .collect(Collectors.toList());

            // Place에 이미지 설정
            place.setFiles(transformedImages);
        }

        System.out.println("post = " + post);
        return post;
    }

    /** 사용자 고유 ID(userId)로 해당 사용자의 모든 포스트를 조회하는 메서드 */
    public List<PostDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /** 모든 포스트를 조회하는 메서드 */
    public List<PostDTO> getAllPosts() {
        List<PostDTO> posts = postMapper.getAllPostsWithDetails();

        for (PostDTO post : posts) {
            // 프로필 이미지 URL 변환
            post.setProfileImageUrl(imageFileService.findImageDownloadLinkByFileUrl(post.getProfileImageUrl()));

            // 대표 이미지 URL 변환
            post.setThumbNailUrl(imageFileService.findImageDownloadLinkByFileUrl(post.getThumbNailUrl()));
        }

        return posts;
    }

    /** 복잡한 조건의 게시물을 검색하는 메서드 */
    public List<PostDTO> searchPosts(LocalDateTime startDate, LocalDateTime endDate, List<Long> userIds, boolean isOpened) {
        List<Post> posts = postRepository.findByWrittenDatetimeBetweenAndUserIdInAndIsOpened(startDate, endDate, userIds, isOpened);
        return posts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    // Post 헬퍼 메서드
    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO(post);

        if(post.getTripId() != null){
            dto.setTripId(post.getTripId());
        }

        return dto;
    }

    /**
     * ### PostCommentDTO ###
     */
    /** 포스트 댓글 고유 ID로 포스팅 댓글을 검색하는 메서드 */
    public PostCommentDTO getPostCommentById(Long id) {
        PostComment comment = postCommentRepository.findById(id)
                .orElseThrow(() -> new PostCommentNotFoundException(id));
        return new PostCommentDTO(comment);
    }

    /** 포스트 고유 ID(postId)로 포스트 댓글 전체를 조회하는 메서드 */
    public List<PostCommentDTO> getPostCommentsByPostId(Long postId) {
        List<PostCommentDTO> postComments = postCommentRepository.findByPostId(postId);

        postComments.forEach(comment -> {
            if (comment.getProfileImageUrl() != null) {
                String convertedUrl = imageFileService.findImageDownloadLinkByFileUrl(comment.getProfileImageUrl());
                comment.setProfileImageUrl(convertedUrl);
            }
        });

        return postComments;
    }

    /** 모든 포스트 댓글을 조회하는 메서드 */
    public List<PostCommentDTO> getAllPostComments() {
        List<PostComment> postComments = postCommentRepository.findAll();
        return postComments.stream()
                .map(PostCommentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * ### PostCoCommentDTO ###
     */
    /** 포스트 댓글 고유 ID로 포스팅 대댓글을 검색하는 메서드 */
    public PostCoCommentDTO getPostCoCommentById(Long id) {
        PostCoComment coComment = postCoCommentRepository.findById(id)
                .orElseThrow(() -> new PostCoCommentNotFoundException(id));
        return new PostCoCommentDTO(coComment);
    }

    /** 포스트 댓글 고유 ID(userId)로 해당 사용자의 모든 포스트 대댓글을 조회하는 메서드 */
    public List<PostCoCommentDTO> getPostCoCommentsByPostCommentId(Long postCommentId) {

        List<PostCoComment> postCoComments = postCoCommentRepository.findByPostCommentId(postCommentId);
        return postCoComments.stream()
                .map(PostCoCommentDTO::new)
                .collect(Collectors.toList());
    }

    /** 모든 포스트 대댓글을 조회하는 메서드 */
    public List<PostCoCommentDTO> getAllPostCoComments() {
        List<PostCoComment> postCoComments = postCoCommentRepository.findAll();
        return postCoComments.stream()
                .map(PostCoCommentDTO::new)
                .collect(Collectors.toList());
    }

    /** 포스트 댓글을 통해 포스트 대댓글을 조회하는 메서드 */
    private List<PostCoCommentDTO> getCoCommentsForComment(Long commentId) {
        List<PostCoComment> coComments = postCoCommentRepository.findByPostCommentId(commentId);
        return coComments.stream()
                .map(PostCoCommentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * ### PostReportDTO ###
     */
    /** 포스트 신고 고유 ID로 포스팅 신고를 검색하는 메서드 */
    public PostReportDTO getPostReportById(Long id) {
        PostReport postReport = postReportRepository.findById(id)
                .orElseThrow(() -> new PostReportNotFoundException(id));
        return new PostReportDTO(postReport);
    }

    /** 사용자 고유 ID(userId)로 해당 사용자의 모든 포스트 신고를 조회하는 메서드 */
    public List<PostReportDTO> getPostReportsByUserId(Long userId) {
        List<PostReport> postReports = postReportRepository.findByUserId(userId);
        return postReports.stream()
                .map(PostReportDTO::new)
                .collect(Collectors.toList());
    }

    /** 특정 포스트에 대한 모든 포스트 신고를 조회하는 메서드 */
    public List<PostReportDTO> getPostReportsByPostId(Long postId) {
        List<PostReport> postReports = postReportRepository.findByPostId(postId);
        return postReports.stream()
                .map(PostReportDTO::new)
                .collect(Collectors.toList());
    }

    /** 모든 포스트 신고를 조회하는 메서드 */
    public List<PostReportDTO> getAllPostReports() {
        List<PostReport> postReports = postReportRepository.findAll();
        return postReports.stream()
                .map(PostReportDTO::new)
                .collect(Collectors.toList());
    }

    /** 처리되지 않은 포스트 신고를 조회하는 메서드 */
    public List<PostReportDTO> getUnprocessedPostReports() {
        List<PostReport> unprocessedReports = postReportRepository.findByIsReportedFalse();
        return unprocessedReports.stream()
                .map(PostReportDTO::new)
                .collect(Collectors.toList());
    }
}