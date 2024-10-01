package com.noblesse.backend.notice.service;

import com.noblesse.backend.notice.domain.Notice;
import com.noblesse.backend.notice.dto.NewNoticeDTO;
import com.noblesse.backend.notice.dto.NoticeDTO;
import com.noblesse.backend.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeDTO registNotice(NewNoticeDTO newNoticeDTO) {
        Notice notice = new Notice(newNoticeDTO.getTitle(), newNoticeDTO.getContent());
        notice = noticeRepository.save(notice);
        return convertToDTO(notice);
    }

    @Override
    public NoticeDTO updateNotice(Long id, NewNoticeDTO newNoticeDTO) {
        Notice foundNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found notice with id: " + id));

        foundNotice.setTitle(newNoticeDTO.getTitle());
        foundNotice.setContent(newNoticeDTO.getContent());

        foundNotice = noticeRepository.save(foundNotice);
        return convertToDTO(foundNotice);
    }

    @Override
    public void deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new RuntimeException("Not found notice with id: " + id);
        }
        noticeRepository.deleteById(id);
    }

    @Override
    public Page<NoticeDTO> getAllNotices(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        return noticePage.map(this::convertToDTO);
    }

    @Override
    public NoticeDTO getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found notice with id: " + id));
        return convertToDTO(notice);
    }

    private NoticeDTO convertToDTO(Notice notice) {
        return new NoticeDTO(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getUpdatedAt(),
                notice.getAuthor()
        );
    }
}
