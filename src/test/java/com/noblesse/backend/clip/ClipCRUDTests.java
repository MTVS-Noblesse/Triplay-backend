package com.noblesse.backend.clip;

import com.noblesse.backend.clip.domain.Clip;
import com.noblesse.backend.clip.dto.ClipRegistRequestDTO;
import com.noblesse.backend.clip.service.ClipService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@SpringBootTest
@Transactional
public class ClipCRUDTests {

    @Autowired
    private ClipService clipService;

    private static Stream<Arguments> newClip() {
        return Stream.of(
                Arguments.of(new ClipRegistRequestDTO(null, "클립1", "클립URL주소1", true, 1L, 1L, null)),
                Arguments.of(new ClipRegistRequestDTO(null, "클립2", "클립URL주소2", true, 2L, 2L, null))
        );
    }

    @DisplayName("클립 추가 테스트")
    @ParameterizedTest
    @MethodSource("newClip")
    void registClip(ClipRegistRequestDTO clipRegistRequestDTO) {
        Assertions.assertDoesNotThrow(() -> {
            clipService.insertClip(clipRegistRequestDTO);
        });
    }

    @DisplayName("클립 조회 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2,3,4,5})
    void findClipByClipId(Long clipId) {
        Assertions.assertDoesNotThrow(() -> {
            Clip clip = clipService.findClipByClipId(clipId);
            System.out.println("clip = " + clip);
        });
    }

    @DisplayName("클립 전체 조회 테스트")
    @Test
    void findAllClip() {
        Assertions.assertDoesNotThrow(() -> {
            clipService.findAll().forEach(System.out::println);
        });
    }


    @DisplayName("클립 수정 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void modifyClip(long clipId) {
        Assertions.assertDoesNotThrow(() -> {
            clipService.updateClipByClipIdForExposeYN(clipId);
        });
    }

    @DisplayName("클립 삭제 테스트")
    @ParameterizedTest
    @ValueSource(longs = {1,2})
    void deleteClip(long clipId) {
        Assertions.assertDoesNotThrow(() -> {
            clipService.deleteClipByClipId(clipId);
        });
    }
}
