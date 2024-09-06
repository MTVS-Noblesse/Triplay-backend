package com.noblesse.backend.notice.service;

import com.noblesse.backend.notice.domain.Notice;
import com.noblesse.backend.notice.dto.NewNoticeDTO;

import java.util.List;

public interface NoticeService {
    void registNotice(Notice notice);
    void updateNotice(Long id, NewNoticeDTO newNoticeDTO);
    void deleteNotice(Long id);
    List<Notice> getAllNotices();
    Notice getNotice(Long id);
}
