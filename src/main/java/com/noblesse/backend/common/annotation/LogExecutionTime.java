package com.noblesse.backend.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})  // 메서드/클래스 레벨에서 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임에도 애노테이션 정보가 유지됨
public @interface LogExecutionTime {
    String value() default ""; // 추가 설명
    boolean includeParams() default true; // 파라미터 포함 여부
    boolean includeResult() default true; // 결과 포함 여부
    long threshold() default 0L; // 로깅할 실행 시간 임계값 (밀리초)
    boolean maskSensitiveData() default true;
}
