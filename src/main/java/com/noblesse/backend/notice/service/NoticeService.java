package com.noblesse.backend.notice.service;

import com.noblesse.backend.notice.domain.Notice;
import com.noblesse.backend.notice.dto.NewNoticeDTO;

import java.util.List;

public interface NoticeService {
    Notice registNotice(Notice notice);
    NewNoticeDTO updateNotice(NewNoticeDTO newNoticeDTO);
    void deleteNotice(Long id);
    List<Notice> getAllNotices();
    Notice getNotice(Long id);
}
