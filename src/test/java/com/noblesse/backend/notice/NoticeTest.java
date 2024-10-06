package com.noblesse.backend.notice;

import com.noblesse.backend.notice.dto.NewNoticeDTO;
import com.noblesse.backend.notice.dto.NoticeDTO;
import com.noblesse.backend.notice.repository.NoticeRepository;
import com.noblesse.backend.notice.service.NoticeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoticeTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeService noticeService;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        noticeRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE notice AUTO_INCREMENT = 1").executeUpdate();
        entityManager.flush();
    }

    @Test
    @DisplayName("#01. 공지사항 등록 테스트")
    @Order(1)
    @Transactional
    void registNoticeTest() {
        noticeService.registNotice(new NewNoticeDTO(null, "notice1", "this is notice1"));
        noticeService.registNotice(new NewNoticeDTO(null, "notice2", "this is notice2"));
        noticeService.registNotice(new NewNoticeDTO(null, "notice3", "this is notice3"));

        assertThat(noticeRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("#02. 공지사항 id로 조회 테스트")
    @Order(2)
    @Transactional
    void findNoticeByIdTest() {
        NoticeDTO notice = noticeService.registNotice(new NewNoticeDTO(null, "notice2", "this is notice2"));

        NoticeDTO foundNotice = noticeService.getNotice(notice.getId());
        assertThat(foundNotice.getId()).isEqualTo(notice.getId());
        assertThat(foundNotice.getContent()).isEqualTo("this is notice2");
    }

    @Test
    @DisplayName("#03. 존재하지 않는 공지사항 조회 시 예외 발생 테스트")
    @Order(3)
    @Transactional
    void findNonExistentNoticeTest() {
        assertThatThrownBy(() -> noticeService.getNotice(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Not found notice with id: 999");
    }

    @Test
    @DisplayName("#04. 공지사항 모두 조회 테스트 (페이징)")
    @Order(4)
    @Transactional
    void findAllNoticeTest() {
        for (int i = 1; i <= 20; i++) {
            noticeService.registNotice(new NewNoticeDTO(null, "notice" + i, "this is notice" + i));
        }

        Page<NoticeDTO> noticePage = noticeService.getAllNotices(0, 10, "createdAt");
        assertThat(noticePage.getTotalElements()).isEqualTo(20);
        assertThat(noticePage.getContent().size()).isEqualTo(10);
        assertThat(noticePage.getContent().get(0).getTitle()).isEqualTo("notice1");
    }

    @Test
    @DisplayName("#05. 공지사항 정렬 테스트")
    @Order(5)
    @Transactional
    void sortNoticeTest() {
        noticeService.registNotice(new NewNoticeDTO(null, "B notice", "content B"));
        noticeService.registNotice(new NewNoticeDTO(null, "A notice", "content A"));
        noticeService.registNotice(new NewNoticeDTO(null, "C notice", "content C"));

        Page<NoticeDTO> noticePage = noticeService.getAllNotices(0, 10, "title");
        assertThat(noticePage.getContent().get(0).getTitle()).isEqualTo("A notice");
        assertThat(noticePage.getContent().get(2).getTitle()).isEqualTo("C notice");
    }

    @Test
    @DisplayName("#06. 공지사항 업데이트 테스트")
    @Order(6)
    @Transactional
    void updateNoticeTest() {
        NoticeDTO notice = noticeService.registNotice(new NewNoticeDTO(null, "original title", "original content"));

        NewNoticeDTO updateDto = new NewNoticeDTO(null, "updated title", "updated content");
        NoticeDTO updatedNotice = noticeService.updateNotice(notice.getId(), updateDto);

        assertThat(updatedNotice.getTitle()).isEqualTo("updated title");
        assertThat(updatedNotice.getContent()).isEqualTo("updated content");
    }

    @Test
    @DisplayName("#07. 공지사항 id로 삭제 테스트")
    @Order(7)
    @Transactional
    void deleteNoticeByIdTest() {
        NoticeDTO notice1 = noticeService.registNotice(new NewNoticeDTO(null, "notice1", "this is notice1"));
        noticeService.registNotice(new NewNoticeDTO(null, "notice2", "this is notice2"));
        noticeService.registNotice(new NewNoticeDTO(null, "notice3", "this is notice3"));

        assertThat(noticeRepository.count()).isEqualTo(3);

        noticeService.deleteNotice(notice1.getId());
        assertThat(noticeRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("#08. 존재하지 않는 공지사항 삭제 시 예외 발생 테스트")
    @Order(8)
    @Transactional
    void deleteNonExistentNoticeTest() {
        assertThatThrownBy(() -> noticeService.deleteNotice(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Not found notice with id: 999");
    }
}