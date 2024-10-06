package com.noblesse.backend.notice.controller;

import com.noblesse.backend.notice.dto.NewNoticeDTO;
import com.noblesse.backend.notice.dto.NoticeDTO;
import com.noblesse.backend.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDTO> getNotice(@PathVariable("noticeId") long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @PostMapping
    public ResponseEntity<NoticeDTO> registerNotice(@RequestBody NewNoticeDTO newNoticeDTO) {
        NoticeDTO createdNotice = noticeService.registNotice(newNoticeDTO);
        return new ResponseEntity<>(createdNotice, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<NoticeDTO>> getAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        return ResponseEntity.ok(noticeService.getAllNotices(page, size, sortBy));
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable("noticeId") Long noticeId, @RequestBody NewNoticeDTO newNoticeDTO) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, newNoticeDTO));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable("noticeId") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
