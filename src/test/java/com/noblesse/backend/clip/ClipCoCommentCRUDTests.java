package com.noblesse.backend.clip;

import com.noblesse.backend.clip.dto.ClipCoCommentRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipCoCommentService;
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
public class ClipCoCommentCRUDTests {

    @Autowired
    private ClipCoCommentService clipCoCommentService;

    private static Stream<Arguments> newClipCoComment() {
        return Stream.of(
                Arguments.of(new ClipCoCommentRegistRequestDTO("클립 대댓글 1", 1L, 1L)),
                Arguments.of(new ClipCoCommentRegistRequestDTO("클립 대댓글 2", 2L, 2L))
        );
    }

    @DisplayName("클립 대댓글 추가 테스트")
    @ParameterizedTest
    @MethodSource("newClipCoComment")
    @Order(1)
    void registClipCoComment(ClipCoCommentRegistRequestDTO clipCoCommentRegistRequestDTO) {
        Assertions.assertDoesNotThrow(() -> {
            clipCoCommentService.registClipCoComment(clipCoCommentRegistRequestDTO);
            clipCoCommentService.findAll().forEach(System.out::println);
        });
    }

    @DisplayName("클립 대댓글 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(2)
    void findClipCoCommentByClipCoCommentId(Long clipCoCommentId) {
        Assertions.assertDoesNotThrow(() -> {
            System.out.println(clipCoCommentService.findClipCoCommentByClipCoCommentId(clipCoCommentId));
        });
    }

    @DisplayName("클립 댓글 번호에 따른 클립 대댓글 전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(3)
    void findAllClipCoCommentsByClipCommentId(Long clipCommentId) {
        Assertions.assertDoesNotThrow(() -> {
            clipCoCommentService.findAllClipCoCOmmentByClipCommentId(clipCommentId).forEach(System.out::println);
        });
    }

    @DisplayName("클립 대댓글 수정 테스트")
    @ParameterizedTest
    @CsvSource({"1,클립대댓글수정1", "2,클립대댓글수정2"})
    @Order(4)
    void modifyClipComment(Long clipCommentId, String newContent) {
        clipCoCommentService.findAll().forEach(System.out::println);
        Assertions.assertDoesNotThrow(() -> {
            clipCoCommentService.updateClipCoCommentByClipCoCommentId(clipCommentId, newContent);
        });
    }

    @DisplayName("클립 삭제 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(5)
    void deleteClipComment(Long clipCoCommentId) {
        Assertions.assertDoesNotThrow(() -> {
            clipCoCommentService.deleteClipCoCommentByClipCoCommentId(clipCoCommentId);
        });
    }
}
