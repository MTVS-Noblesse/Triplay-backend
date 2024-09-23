package com.noblesse.backend.notice.repository;

import com.noblesse.backend.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
