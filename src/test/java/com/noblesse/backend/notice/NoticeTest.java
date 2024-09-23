package com.noblesse.backend.notice;

import com.noblesse.backend.notice.domain.Notice;
import com.noblesse.backend.notice.repository.NoticeRepository;
import com.noblesse.backend.notice.service.NoticeServiceImpl;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class NoticeTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeServiceImpl noticeServiceImpl;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        noticeRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE notice AUTO_INCREMENT = 1").executeUpdate();
        entityManager.flush();
    }

    @DisplayName("공지사항 등록 테스트")
    @Test
    @Transactional
    void registNoticeTest() {
        noticeServiceImpl.registNotice(new Notice("notice1", "this is notice1"));
        noticeServiceImpl.registNotice(new Notice("notice2", "this is notice2"));
        noticeServiceImpl.registNotice(new Notice("notice3", "this is notice3"));

        Assertions.assertThat(noticeRepository.count()).isEqualTo(3);
    }

    @DisplayName("공지사항 id로 조회 테스트")
    @Test
    @Transactional
    void findNoticeByIdTest() {
        noticeServiceImpl.registNotice(new Notice("notice1", "this is notice1"));
        noticeServiceImpl.registNotice(new Notice("notice2", "this is notice2"));
        noticeServiceImpl.registNotice(new Notice("notice3", "this is notice3"));

        Notice foundNotice = noticeServiceImpl.getNotice(2L);
        Assertions.assertThat(foundNotice.getId()).isEqualTo(2);
        Assertions.assertThat(foundNotice.getContent()).isEqualTo("this is notice2");
    }

    @DisplayName("공지사항 모두 조회 테스트")
    @Test
    @Transactional
    void findAllNoticeTest() {
        noticeServiceImpl.registNotice(new Notice("notice1", "this is notice1"));
        noticeServiceImpl.registNotice(new Notice("notice2", "this is notice2"));
        noticeServiceImpl.registNotice(new Notice("notice3", "this is notice3"));

        List<Notice> noticeList = noticeServiceImpl.getAllNotices();
        Assertions.assertThat(noticeList.size()).isEqualTo(3);
        Assertions.assertThat(noticeList.get(0).getContent()).isEqualTo("this is notice1");
        Assertions.assertThat(noticeList.get(1).getContent()).isEqualTo("this is notice2");
        Assertions.assertThat(noticeList.get(2).getContent()).isEqualTo("this is notice3");
    }

    @DisplayName("공지사항 id로 삭제 테스트")
    @Test
    @Transactional
    void deleteNoticeByIdTest() {
        noticeServiceImpl.registNotice(new Notice("notice1", "this is notice1"));
        noticeServiceImpl.registNotice(new Notice("notice2", "this is notice2"));
        noticeServiceImpl.registNotice(new Notice("notice3", "this is notice3"));

        Assertions.assertThat(noticeRepository.count()).isEqualTo(3);

        noticeServiceImpl.deleteNotice(2L);
        Assertions.assertThat(noticeRepository.count()).isEqualTo(2);
    }
}
