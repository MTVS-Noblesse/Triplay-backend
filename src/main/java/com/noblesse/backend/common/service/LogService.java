package com.noblesse.backend.common.service;

import com.noblesse.backend.common.dto.LogDTO;
import com.noblesse.backend.common.entity.LogEntity;
import com.noblesse.backend.common.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional
    public void saveLog(LogDTO logDto) {
        LogEntity logEntity = LogEntity.builder()
                .className(logDto.getClassName())
                .methodName(logDto.getMethodName())
                .params(logDto.getParams())
                .result(String.valueOf(logDto.getResult()))
                .executionTime(logDto.getExecutionTime())
                .errorMessage(logDto.getErrorMessage())
                .build();

        logRepository.save(logEntity);
    }

    @Transactional(readOnly = true)
    public List<LogEntity> getLogsByTimeRange(LocalDateTime start, LocalDateTime end) {
        return logRepository.findByCreatedAtBetween(start, end);
    }

    @Transactional(readOnly = true)
    public List<LogEntity> getLogsByExecutionTimeThreshold(Long threshold) {
        return logRepository.findByExecutionTimeGreaterThan(threshold);
    }

    @Transactional(readOnly = true)
    public List<LogEntity> getLogsByClassAndMethod(String className, String methodName) {
        return logRepository.findByClassNameAndMethodName(className, methodName);
    }

    @Transactional(readOnly = true)
    public Page<LogEntity> getAllLogs(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<LogEntity> logPage = logRepository.findAll(pageable);
        return logPage;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPerformanceStats(LocalDateTime start, LocalDateTime end) {
        List<LogEntity> logs = logRepository.findByCreatedAtBetween(start, end);

        Map<String, Object> stats = new HashMap<>();

        // 평균 실행 시간 계산
        DoubleSummaryStatistics executionStats = logs.stream()
                .mapToDouble(LogEntity::getExecutionTime)
                .summaryStatistics();

        stats.put("avgExecutionTime", executionStats.getAverage());
        stats.put("maxExecutionTime", executionStats.getMax());
        stats.put("minExecutionTime", executionStats.getMin());

        // 도메인별 통계
        Map<String, Double> domainStats = logs.stream()
                .collect(Collectors.groupingBy(
                        LogEntity::getClassName,
                        Collectors.averagingDouble(LogEntity::getExecutionTime)
                ));
        stats.put("domainStats", domainStats);

        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getErrorStats(LocalDateTime start, LocalDateTime end) {
        List<LogEntity> errorLogs = logRepository.findByErrorMessageIsNotNullAndCreatedAtBetween(start, end);

        Map<String, Object> stats = new HashMap<>();

        // 에러 발생 횟수
        stats.put("totalErrors", errorLogs.size());

        // 도메인별 에러 통계
        Map<String, Long> errorsByDomain = errorLogs.stream()
                .collect(Collectors.groupingBy(
                        LogEntity::getClassName,
                        Collectors.counting()
                ));
        stats.put("errorsByDomain", errorsByDomain);

        return stats;
    }
}
