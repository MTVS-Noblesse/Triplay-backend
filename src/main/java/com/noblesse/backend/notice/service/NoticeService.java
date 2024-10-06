package com.noblesse.backend.notice.service;

import com.noblesse.backend.notice.dto.NewNoticeDTO;
import com.noblesse.backend.notice.dto.NoticeDTO;
import org.springframework.data.domain.Page;

public interface NoticeService {
    NoticeDTO registNotice(NewNoticeDTO newNoticeDTO);
    NoticeDTO updateNotice(Long id, NewNoticeDTO newNoticeDTO);
    void deleteNotice(Long id);
    Page<NoticeDTO> getAllNotices(int page, int size, String sortBy);
    NoticeDTO getNotice(Long id);
}
