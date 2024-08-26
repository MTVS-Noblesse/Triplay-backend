package com.noblesse.backend.clip;

import com.noblesse.backend.clip.dto.ClipReportRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipReportService;
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
public class ClipReportCRUDTests {

    @Autowired
    private ClipReportService clipReportService;

    private static Stream<Arguments> newClipReport() {
        return Stream.of(
                Arguments.of(new ClipReportRegistRequestDTO(1L, "클립 신고 제목1", "클립 신고 내용1", 1L, 1L)),
                Arguments.of(new ClipReportRegistRequestDTO(2L, "클립 신고 제목2", "클립 신고 내용2", 2L, 2L))
        );
    }

    @DisplayName("클립 대댓글 추가 테스트")
    @ParameterizedTest
    @MethodSource("newClipReport")
    @Order(1)
    void registClipReport(ClipReportRegistRequestDTO clipReportRegistRequestDTO) {
        Assertions.assertDoesNotThrow(() -> {
            clipReportService.registClipReport(clipReportRegistRequestDTO);
            clipReportService.findAll().forEach(System.out::println);
        });
    }

    @DisplayName("클립 대댓글 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(2)
    void findClipReportByClipReportId(Long clipReportId) {
        Assertions.assertDoesNotThrow(() -> {
            System.out.println(clipReportService.findClipReportByClipReportId(clipReportId));
        });
    }

    @DisplayName("클립 댓글 번호에 따른 클립 대댓글 전체 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(3)
    void findAllClipReportByClipId(Long clipId) {
        Assertions.assertDoesNotThrow(() -> {
            clipReportService.findClipReportsByClipId(clipId).forEach(System.out::println);
        });
    }

    @DisplayName("클립 대댓글 수정 테스트")
    @ParameterizedTest
    @CsvSource({"1,클립신고제목수정1,클립신고내용수정1", "2,클립신고제목수정2,클립대댓글수정2"})
    @Order(4)
    void modifyClipReport(Long clipCommentId, String newTitle, String newContent) {
        clipReportService.findAll().forEach(System.out::println);
        Assertions.assertDoesNotThrow(() -> {
            clipReportService.updateClipReport(clipCommentId, newTitle, newContent);
        });
    }

    @DisplayName("클립 삭제 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    @Order(5)
    void deleteClipReport(Long clipReportId) {
        Assertions.assertDoesNotThrow(() -> {
            clipReportService.deleteClipReportByClipReportId(clipReportId);
        });
    }
}
