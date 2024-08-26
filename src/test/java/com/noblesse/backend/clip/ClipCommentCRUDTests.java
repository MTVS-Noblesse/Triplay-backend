package com.noblesse.backend.clip;

import com.noblesse.backend.clip.dto.ClipCommentRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipCommentService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClipCommentCRUDTests {
    @Autowired
    private ClipCommentService clipCommentService;

    private static Stream<Arguments> newClipComment() {
        return Stream.of(
                Arguments.of(new ClipCommentRegistRequestDTO("클립 댓글 1", 1L, 1L)),
                Arguments.of(new ClipCommentRegistRequestDTO("클립 댓글 2", 2L, 2L))
        );
    }

    @DisplayName("클립 댓글 추가 테스트")
    @ParameterizedTest
    @MethodSource("newClipComment")
    @Order(1)
    void registClip(ClipCommentRegistRequestDTO clipCommentRegistRequestDTO) {
        Assertions.assertDoesNotThrow(() -> {
            clipCommentService.registClipComment(clipCommentRegistRequestDTO);
            clipCommentService.findAll().forEach(System.out::println);
        });
    }

    @DisplayName("클립 댓글 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(2)
    void findClipCommentByClipCommentId(Long clipCommentId) {
        Assertions.assertDoesNotThrow(() -> {
            System.out.println(clipCommentService.findClipCommentByClipCommentId(clipCommentId));
        });
    }

    @DisplayName("클립 번호에 따른 클립 전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(3)
    void findAllClipComments(Long clipId) {
        Assertions.assertDoesNotThrow(() -> {
            clipCommentService.findAllClipCommentByClipId(clipId).forEach(System.out::println);
        });
    }

    @DisplayName("클립 수정 테스트")
    @ParameterizedTest
    @CsvSource({"1,클립댓글수정1", "2,클립댓글수정2"})
    @Order(4)
    void modifyClipComment(Long clipCommentId, String newContent) {
        clipCommentService.findAll().forEach(System.out::println);
        Assertions.assertDoesNotThrow(() -> {
            clipCommentService.updateClipComment(clipCommentId, newContent);
        });
    }

    @DisplayName("클립 삭제 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(5)
    void deleteClipComment(Long clipCommentId) {
        Assertions.assertDoesNotThrow(() -> {
            clipCommentService.deleteClipByClipId(clipCommentId);
        });
    }
}
