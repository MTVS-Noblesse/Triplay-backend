package com.noblesse.backend.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noblesse.backend.common.annotation.LogExecutionTime;
import com.noblesse.backend.common.dto.LogDTO;
import com.noblesse.backend.common.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;
    private final LogService logService;

    // 마스킹할 민감한 필드명들을 Set으로 정의
    private static final Set<String> SENSITIVE_FIELDS = new HashSet<>(Arrays.asList(
            "password",
            "token",
            "accessToken",
            "refreshToken",
            "secret",
            "credential",
            "authorization",
            "key"
    ));

    // 이메일 패턴 정규식
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)");

    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();  // return 문 제거
        } catch (Exception e) {
            log.error("Exception occurred in {}.{}: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();

            // 파라미터를 JSON 문자열로 변환하고 마스킹 처리
            String params = maskSensitiveData(objectMapper.writeValueAsString(joinPoint.getArgs()));

            // 결과값도 마스킹 처리
            String maskedResult = (result != null) ?
                    maskSensitiveData(objectMapper.writeValueAsString(result)) : null;

            LogDTO logDto = LogDTO.builder()
                    .className(className)
                    .methodName(methodName)
                    .params(params)
                    .result(maskedResult)
                    .executionTime(executionTime)
                    .build();

            log.info("Method Execution Log: {}", logDto);
            logService.saveLog(logDto);  // DB에 로그 저장
        }

        return result;
    }

    /** 민감한 정보 필터링 로직 메서드 */
    private String maskSensitiveData(String jsonString) {
        if (jsonString == null) return null;

        // 민감한 필드 마스킹
        for (String field : SENSITIVE_FIELDS) {
            // JSON 필드 패턴에 맞춰 마스킹
            jsonString = jsonString.replaceAll(
                    String.format("\"%s\":\"[^\"]*\"", field),
                    String.format("\"%s\":\"*****\"", field)
            );
            // URL 파라미터 형식의 데이터 마스킹
            jsonString = jsonString.replaceAll(
                    String.format("%s=[^&\\s]+", field),
                    String.format("%s=*****", field)
            );
        }

        // 이메일 마스킹 (예: test@example.com -> t***@e***.com)
        jsonString = EMAIL_PATTERN.matcher(jsonString).replaceAll(email -> {
            String[] parts = email.group(1).split("@");
            if (parts.length == 2) {
                String localPart = parts[0];
                String domain = parts[1];
                String maskedLocal = localPart.substring(0, 1) + "*".repeat(localPart.length() - 1);
                String[] domainParts = domain.split("\\.");
                String maskedDomain = domainParts[0].substring(0, 1) + "*".repeat(domainParts[0].length() - 1);
                return maskedLocal + "@" + maskedDomain + "." + domainParts[1];
            }
            return email.group(1);
        });

        return jsonString;
    }

    /** JWT 토큰 마스킹을 위한 추가 메서드 */
    private String maskJwtToken(String token) {
        if (token == null || token.length() < 10) return token;
        // 토큰의 처음 5자와 마지막 5자만 보여주고 나머지는 마스킹
        return token.substring(0, 5) + "*".repeat(token.length() - 10) + token.substring(token.length() - 5);
    }
}
