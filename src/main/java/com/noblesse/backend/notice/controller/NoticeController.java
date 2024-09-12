package com.noblesse.backend.notice.controller;

import com.noblesse.backend.notice.domain.Notice;
import com.noblesse.backend.notice.dto.NewNoticeDTO;
import com.noblesse.backend.notice.service.NoticeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeServiceImpl noticeServiceImpl;

    @Autowired
    public NoticeController(NoticeServiceImpl noticeServiceImpl) {
        this.noticeServiceImpl = noticeServiceImpl;
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getNotice(@PathVariable("noticeId") long noticeId) {
        return ResponseEntity.ok(noticeServiceImpl.getNotice(noticeId));
    }

    @PostMapping
    public ResponseEntity<Notice> registerNotice(@RequestBody Notice notice) {
        return ResponseEntity.ok(noticeServiceImpl.registNotice(notice));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeServiceImpl.getAllNotices());
    }

    @PatchMapping
    public ResponseEntity<NewNoticeDTO> updateNotice(@RequestBody NewNoticeDTO nnDTO) {
        return ResponseEntity.ok(noticeServiceImpl.updateNotice(nnDTO));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Notice> deleteNotice(@PathVariable("noticeId") long noticeId) {
        noticeServiceImpl.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
