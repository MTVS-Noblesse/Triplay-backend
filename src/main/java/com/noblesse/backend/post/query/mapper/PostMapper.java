package com.noblesse.backend.post.query.mapper;

import com.noblesse.backend.post.common.dto.PostDTO;
import com.noblesse.backend.trip.dto.PlaceDTO;
import com.noblesse.backend.file.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    // 모든 포스트와 관련된 정보 가져오기
    List<PostDTO> getAllPostsWithDetails();

    // 고유 id로 포스트 조회
    PostDTO getPostByPostId(@Param("postId") Long postId);

    // 특정 Trip에 속한 Place 정보 가져오기
    List<PlaceDTO> getPlacesByTripId(@Param("tripId") Long tripId);

    // 특정 Post 및 Place에 연결된 이미지 가져오기
    List<FileDTO> getImagesByPostAndPlaceIds(@Param("postId") Long postId, @Param("tripId") Long tripId);

}
