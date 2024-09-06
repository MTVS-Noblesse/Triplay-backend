package com.noblesse.backend.notice.service;

import com.noblesse.backend.notice.domain.Notice;
import com.noblesse.backend.notice.dto.NewNoticeDTO;
import com.noblesse.backend.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService{

    final private NoticeRepository noticeRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @Override
    public void registNotice(Notice notice) {
        noticeRepository.save(notice);
    }

    @Override
    public void updateNotice(Long id, NewNoticeDTO newNoticeDTO) {
        Notice foundNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found notice with id: " + id));

        foundNotice.setTitle(newNoticeDTO.getTitle());
        foundNotice.setContent(newNoticeDTO.getContent());

        noticeRepository.save(foundNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    @Override
    public Notice getNotice(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found notice with id: " + id));
    }
}
