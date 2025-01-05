package com.itwillbs.c4d2412t3p1.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.service.LogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

// 메서드 실행 후 로그를 기록
@RequiredArgsConstructor
@Aspect
@Component
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	private final LogService logService;
	// 클라이언트 요청 정보를 가져오기 위한 객체
	private final HttpServletRequest request;
	// 객체를 JSON으로 변환하기 위한 ObjectMapper
	private final ObjectMapper objectMapper = new ObjectMapper();
	// 스레드별로 로그 정보를 임시 저장하기 위한 ThreadLocal
	private final ThreadLocal<LogDTO> logThreadLocal = new ThreadLocal<>();

//	 @LogActivity 어노테이션이 부착된 메서드 실행 전에 동작 메서드의 인자 정보를 JSON으로 변환하여 ThreadLocal에 저장
	@Before("@annotation(logActivity)")
	public void logBefore(JoinPoint joinPoint, LogActivity logActivity) {

		LogDTO logDTO = new LogDTO();
		logDTO.setLog_cd(logService.generateLogCode());
		logDTO.setLog_sj(logActivity.value());
		logDTO.setLog_ju(logActivity.action());
		logDTO.setLog_od(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // 현재 시간 설정
		logDTO.setLog_oi(getClientIp());
		logDTO.setLog_bj(getUserAgent());

		// 메서드의 인자들을 JSON 형식으로 변환하여 로그 상세 정보에 추가
		Object[] args = joinPoint.getArgs();
		Map<String, Object> logDetails = new HashMap<>();
		for (Object arg : args) {
			try {
				String jsonString = objectMapper.writeValueAsString(arg);
				logDetails.put(arg.getClass().getSimpleName(), jsonString);
			} catch (JsonProcessingException e) {
				logger.error("인자 처리 중 오류: {}", e.getMessage());
				logDetails.put(arg.getClass().getSimpleName(), "인자 처리 중 오류: " + e.getMessage());
			}
		}
		 //  logDTO에 상세 정보 설정
		logDTO.setLog_jdMap(logDetails);
		 // ThreadLocal에 logDTO 저장
		logThreadLocal.set(logDTO);
	}

//	 @LogActivity 어노테이션이 부착된 메서드 실행 후에 동작 / ThreadLocal에 저장된 로그 정보를 가져와 실제 로그 저장
	@AfterReturning(value = "@annotation(logActivity)", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, LogActivity logActivity, Object result) {
		// ThreadLocal에서 logDTO 가져오기
		LogDTO logDTO = logThreadLocal.get(); 
		if (logDTO != null) {

			// 메서드의 반환값을 JSON 형식으로 변환하여 logDetails에 추가
			Map<String, Object> logDetails = logDTO.getLog_jdMap();
			try {
				String jsonString = objectMapper.writeValueAsString(result);
				logDetails.put("반환값", jsonString);
			} catch (JsonProcessingException e) {
				logger.error("반환값 처리 중 오류: {}", e.getMessage());
				logDetails.put("반환값", "반환값 처리 중 오류: " + e.getMessage());
			}
			// 업데이트된 상세 정보를 로그 DTO에 설정
			logDTO.setLog_jdMap(logDetails);

			logger.info("Saving LogDTO: {}", logDTO);
			if (logDTO.getLog_cd() == null) {
				logger.error("log_cd is null");
			}
			if (logDTO.getLog_jdMap() == null) {
				logger.error("log_jdMap is null");
			}
//			logService에서 insert 작업 수행
			logService.saveLog(logDTO);
			 // ThreadLocal에서 logDTO 제거하여 메모리 누수 방지
			logThreadLocal.remove();
		}
	}

//	 @LogActivity 어노테이션이 부착된 메서드 실행 중 예외 발생 시 동작 예외 정보를 로깅
	@AfterThrowing(value = "@annotation(logActivity)", throwing = "exception")
	public void logAfterThrowing(JoinPoint joinPoint, LogActivity logActivity, Exception exception) {
//		ThreadLocal에서 logDTO 가져오기
		LogDTO logDTO = logThreadLocal.get();
		if (logDTO != null) {
			logDTO.setLog_ju("작업 실패: " + exception.getMessage()); // 임시(추가 컬럼으로 상태 체크할 것인지 고민중)

			// 예외 정보를 logDetails에 추가
			Map<String, Object> logDetails = logDTO.getLog_jdMap();
			logDetails.put("예외 메시지", exception.getMessage());
			logDetails.put("예외 클래스", exception.getClass().getName());
			logDTO.setLog_jdMap(logDetails); // 업데이트된 상세 정보를 logDTO에 설정
			
			// logSearvice에서 insert 작업 수행(예외 저장)
			logService.saveLog(logDTO);
			// ThreadLocal에서 로그 DTO 제거하여 메모리 누수 방지
			logThreadLocal.remove(); 
		}
		logger.error("메서드 실행 중 예외 발생: {}", exception.getMessage()); // 예외 메시지를 로깅
	}

//	 클라이언트의 IP 주소를 가져오는 메서드
	private String getClientIp() {
		return request.getRemoteAddr();
	}

//	 클라이언트의 브라우저 정보를 가져오는 메서드
	private String getUserAgent() {
		return request.getHeader("User-Agent");
	}
}
