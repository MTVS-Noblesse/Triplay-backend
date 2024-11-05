package com.noblesse.backend.common.controller;

import com.noblesse.backend.common.entity.LogEntity;
import com.noblesse.backend.common.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/time-range")
    public ResponseEntity<List<LogEntity>> getLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(logService.getLogsByTimeRange(start, end));
    }

    @GetMapping("/execution-time")
    public ResponseEntity<List<LogEntity>> getLogsByExecutionTime(
            @RequestParam Long threshold) {
        return ResponseEntity.ok(logService.getLogsByExecutionTimeThreshold(threshold));
    }

    @GetMapping("/method")
    public ResponseEntity<List<LogEntity>> getLogsByClassAndMethod(
            @RequestParam String className,
            @RequestParam String methodName) {
        return ResponseEntity.ok(logService.getLogsByClassAndMethod(className, methodName));
    }

    @GetMapping
    public ResponseEntity<Page<LogEntity>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        return ResponseEntity.ok(logService.getAllLogs(page, size, sortBy));
    }
}
