package com.noblesse.backend.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class LogDTO {
    private String className;
    private String methodName;
    private String params;
    private Object result;
    private Long executionTime;
    private String errorMessage;
}
