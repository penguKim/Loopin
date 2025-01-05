package com.itwillbs.c4d2412t3p1.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.service.LogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 메서드 실행 전후로 로그를 기록하는 Aspect 클래스
 */
@RequiredArgsConstructor
@Aspect
@Component
public class LogAspect {

    private final LogService logService; // 로그 서비스를 통한 로그 저장
    private final HttpServletRequest request; // 클라이언트 요청 정보를 가져오기 위한 객체
    private final ObjectMapper objectMapper = new ObjectMapper(); // 객체를 JSON으로 변환하기 위한 ObjectMapper
    private final ThreadLocal<LogDTO> logThreadLocal = new ThreadLocal<>(); // 스레드별로 로그 정보를 임시 저장하기 위한 ThreadLocal

    /**
     * @LogActivity 어노테이션이 부착된 메서드 실행 전에 동작
     * 메서드의 인자 정보를 JSON으로 변환하여 ThreadLocal에 저장
     */
    @Before("@annotation(logActivity)")
    public void logBefore(JoinPoint joinPoint, LogActivity logActivity) {
        LogDTO logDTO = new LogDTO(); // 새로운 로그 DTO 생성
        logDTO.setLog_cd(logService.generateLogCode()); // 고유한 로그 코드 생성 및 설정
        logDTO.setLog_sj(logActivity.value()); // 로그 제목 설정
        logDTO.setLog_ju(logActivity.action()); // 수행 작업 설정
        logDTO.setLog_od(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // 현재 시간 설정
        logDTO.setLog_oi(getClientIp()); // 클라이언트 IP 설정
        logDTO.setLog_bj(getUserAgent()); // 클라이언트 브라우저 정보 설정

        // 메서드의 인자들을 JSON 형식으로 변환하여 로그 상세 정보에 추가
        Object[] args = joinPoint.getArgs();
        Map<String, Object> logDetails = new HashMap<>();
        for (Object arg : args) {
            try {
                String jsonString = objectMapper.writeValueAsString(arg);
                logDetails.put(arg.getClass().getSimpleName(), jsonString);
            } catch (JsonProcessingException e) {
                logDetails.put(arg.getClass().getSimpleName(), "인자 처리 중 오류: " + e.getMessage());
            }
        }
        logDTO.setLog_jdMap(logDetails); // 로그 DTO에 상세 정보 설정

        logThreadLocal.set(logDTO); // ThreadLocal에 로그 DTO 저장
    }

    /**
     * @LogActivity 어노테이션이 부착된 메서드 실행 후에 동작
     * ThreadLocal에 저장된 로그 정보를 가져와 실제 로그 저장 수행
     */
    @AfterReturning(value = "@annotation(logActivity)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, LogActivity logActivity, Object result) {
        LogDTO logDTO = logThreadLocal.get(); // ThreadLocal에서 로그 DTO 가져오기
        if (logDTO != null) {
            logDTO.setLog_ju("작업 완료"); // 작업 상태를 '작업 완료'로 업데이트

            // 메서드의 반환값을 JSON 형식으로 변환하여 로그 상세 정보에 추가
            Map<String, Object> logDetails = logDTO.getLog_jdMap();
            try {
                String jsonString = objectMapper.writeValueAsString(result);
                logDetails.put("반환값", jsonString);
            } catch (JsonProcessingException e) {
                logDetails.put("반환값", "반환값 처리 중 오류: " + e.getMessage());
            }
            logDTO.setLog_jdMap(logDetails); // 업데이트된 상세 정보를 로그 DTO에 설정

            logService.saveLog(logDTO); // 로그 서비스 통해 로그 저장
            logThreadLocal.remove(); // ThreadLocal에서 로그 DTO 제거하여 메모리 누수 방지
        }
    }

    /**
     * 클라이언트의 IP 주소를 가져오는 메서드
     * @return 클라이언트의 IP 주소
     */
    private String getClientIp() {
        return request.getRemoteAddr();
    }

    /**
     * 클라이언트의 브라우저 정보를 가져오는 메서드
     * @return 클라이언트의 브라우저 정보 (User-Agent 헤더 값)
     */
    private String getUserAgent() {
        return request.getHeader("User-Agent");
    }
}
