package com.noblesse.backend.common.repository;

import com.noblesse.backend.common.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findByClassNameAndMethodName(String className, String methodName);
    List<LogEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<LogEntity> findByExecutionTimeGreaterThan(Long threshold);
    List<LogEntity> findByErrorMessageIsNotNullAndCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    void deleteByCreatedAtBefore(LocalDateTime thirtyDaysAgo);
}
