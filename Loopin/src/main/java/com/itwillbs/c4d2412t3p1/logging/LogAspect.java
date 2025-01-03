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

import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.service.LogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Aspect
@Component
public class LogAspect {

    private final LogService logService;
    private final HttpServletRequest request;

//  	@LogActivity 어노테이션 부착된 메서드 실행 전 동작.
    @Before("@annotation(logActivity)")
    public void logBefore(JoinPoint joinPoint, LogActivity logActivity) {
        LogDTO logDTO = new LogDTO();
        logDTO.setLog_cd(logService.generateLogCode()); // 고유 로그 코드 생성
        logDTO.setLog_sj(logActivity.value());          // 로그 제목
        logDTO.setLog_ju(logActivity.action());         // 수행 작업
        logDTO.setLog_od(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDTO.setLog_oi(getClientIp());                // 클라이언트 IP
        logDTO.setLog_bj(getUserAgent());               // 브라우저 정보

        // 메서드 인자 기록
        Object[] args = joinPoint.getArgs();
        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("methodArgs", args);
        logDTO.setLog_jdMap(logDetails);

        // 로그 저장
        logService.saveLog(logDTO);
    }

//      @LogActivity 어노테이션 부착된 메서드 실행 후 동작.
    @AfterReturning(value = "@annotation(logActivity)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, LogActivity logActivity, Object result) {
        LogDTO logDTO = new LogDTO();
        logDTO.setLog_cd(logService.generateLogCode()); // 고유 로그 코드 생성
        logDTO.setLog_sj(logActivity.value());          // 로그 제목
        logDTO.setLog_ju("Method Completed");           // 작업 완료 메시지
        logDTO.setLog_od(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        logDTO.setLog_oi(getClientIp());                // 클라이언트 IP
        logDTO.setLog_bj(getUserAgent());               // 브라우저 정보

        // 반환값 기록
        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("returnValue", result);
        logDTO.setLog_jdMap(logDetails);

        // 로그 저장
        logService.saveLog(logDTO);
    }

//     클라이언트의 IP 주소를 가져옴.
    private String getClientIp() {
        return request.getRemoteAddr();
    }

//     클라이언트의 브라우저 정보(User-Agent)를 가져옴.
    private String getUserAgent() {
        return request.getHeader("User-Agent");
    }
}
