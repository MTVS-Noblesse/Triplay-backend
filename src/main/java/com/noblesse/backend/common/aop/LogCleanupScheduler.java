package com.noblesse.backend.common.aop;

import com.noblesse.backend.common.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LogCleanupScheduler {

    private final LogRepository logRepository;

    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시에 실행
    public void cleanupOldLogs() {
        // 30일 이전의 로그 삭제
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        logRepository.deleteByCreatedAtBefore(thirtyDaysAgo);
    }
}
