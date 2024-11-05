package com.noblesse.backend.post.query.application.service;

import com.noblesse.backend.file.dto.FileDTO;
import com.noblesse.backend.file.entity.File;
import com.noblesse.backend.file.entity.QFile;
import com.noblesse.backend.file.repository.FileRepository;
import com.noblesse.backend.file.service.FileService;
import com.noblesse.backend.oauth2.entity.OAuthUser;
import com.noblesse.backend.oauth2.entity.QOAuthUser;
import com.noblesse.backend.oauth2.repository.OAuthRepository;
import com.noblesse.backend.post.common.dto.PostCoCommentDTO;
import com.noblesse.backend.post.common.dto.PostCommentDTO;
import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.post.common.dto.PostReportDTO;
import com.noblesse.backend.post.common.entity.*;
import com.noblesse.backend.post.common.exception.PostCoCommentNotFoundException;
import com.noblesse.backend.post.common.exception.PostCommentNotFoundException;
import com.noblesse.backend.post.common.exception.PostNotFoundException;
import com.noblesse.backend.post.common.exception.PostReportNotFoundException;
import com.noblesse.backend.post.query.infrastructure.persistence.repository.*;
import com.noblesse.backend.trip.domain.QTrip;
import com.noblesse.backend.trip.domain.Trip;
import com.noblesse.backend.trip.dto.TripDTO;
import com.noblesse.backend.trip.repository.TripRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostQueryService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCoCommentRepository postCoCommentRepository;
    private final PostReportRepository postReportRepository;
    private final CustomPostRepositoryImpl customPostRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;

    /**
     * ### PostDTO ###
     */
    /** 포스트 고유 ID로 포스팅 검색하는 메서드 */
    public PostDTO getPostById(Long id) {
        Tuple result = customPostRepository.findPostByPostId(id);

        if(result == null) {
            throw new PostNotFoundException(id);
        }

        Post post = result.get(QPost.post);
        String userName = result.get(QOAuthUser.oAuthUser.userName);
        String profileImageUrl = fileService.findImageDownloadLinkByFileUrl(result.get(QFile.file.fileUrl));
        LocalDate tripStartDate = result.get(QTrip.trip.tripStartDate);
        LocalDate tripEndDate = result.get(QTrip.trip.tripEndDate);
        String tripParty = result.get(QTrip.trip.tripParty);

        List<File> postImages = fileRepository.findFilesByPostId(id)
                .stream()
                .sorted(Comparator.comparing(File::getPostImageOrder))
                .collect(Collectors.toList());

        List<FileDTO> fileDTOs = postImages.stream()
                .map(file -> new FileDTO(file, fileService.findImageDownloadLinkByFileUrl(file.getFileUrl())))
                .collect(Collectors.toList());

        PostDTO dto = new PostDTO(post);
        dto.setUserName(userName);
        dto.setProfileImageUrl(profileImageUrl);
        dto.setTripStartDate(tripStartDate);
        dto.setTripEndDate(tripEndDate);
        dto.setTripParty(tripParty);
        dto.setFiles(fileDTOs);

        System.out.println("DTO: " + dto.toString());
        return dto;
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
        List<Tuple> results = customPostRepository.findPostsWithDetails();

        List<PostDTO> posts = new ArrayList<>();

        for (Tuple result : results) {
            Post post = result.get(QPost.post);
            String userName = result.get(QOAuthUser.oAuthUser.userName);
            String profileImageUrl = fileService.findImageDownloadLinkByFileUrl(result.get(new QFile("profileFile").fileUrl));
            LocalDate tripStartDate = result.get(QTrip.trip.tripStartDate);
            LocalDate tripEndDate = result.get(QTrip.trip.tripEndDate);
            String tripParty = result.get(QTrip.trip.tripParty);
            String thumbnailImage =fileService.findImageDownloadLinkByFileUrl(result.get(new QFile("thumbnailFile").fileUrl));

            PostDTO dto = new PostDTO(post);
            dto.setUserName(userName);
            dto.setProfileImageUrl(profileImageUrl);
            dto.setTripStartDate(tripStartDate);
            dto.setTripEndDate(tripEndDate);
            dto.setTripParty(tripParty);
            dto.setThumbnailImageUrl(thumbnailImage);

            posts.add(dto);
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
        List<PostComment> postComments = postCommentRepository.findByPostId(postId);
        return postComments.stream()
                .map(PostCommentDTO::new)
                .collect(Collectors.toList());
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