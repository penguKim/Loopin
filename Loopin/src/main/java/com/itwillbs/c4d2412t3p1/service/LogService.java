package com.itwillbs.c4d2412t3p1.service;

import java.time.LocalDateTime;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.config.FilterRequest.LogFilterRequest;
import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.entity.Log;
import com.itwillbs.c4d2412t3p1.logging.LogConverter;
import com.itwillbs.c4d2412t3p1.repository.LogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@lombok.extern.java.Log
public class LogService {
	private final LogRepository logRepository;
	private final LogConverter logConverter;

//    로그 저장
	public void saveLog(LogDTO logDTO) {
		// SEQUENCE에서 시퀀스 값 가져오기
		Long sequenceValue = logRepository.getNextSequenceValue();
		logDTO.setSequenceValue(sequenceValue);
		// DTO → 엔티티 변환 후 저장
		Log logEntity = logConverter.toEntity(logDTO);
		log.info("저장 중인 LogDTO: {}" + logDTO.toString());
		log.info("저장 중인 LogEntity: {}" + logEntity.toString());
		logRepository.save(logEntity);
	}

	public List<LogDTO> select_LOG() {

		// 조인된 로그 데이터 조회
		List<Log> logs = logRepository.findAllLogsWithEmployee();
		// 엔티티를 DTO로 변환
		return logs.stream()
		        .map(log -> logConverter.setLogDTO(log, true)) // log_jd 포함
		        .collect(Collectors.toList());
	}

	public List<LogDTO> select_FILTERED_LOG(LogFilterRequest filterRequest) {
		
		log.info(filterRequest.toString());
		List<Log> logs = logRepository.findLogsByFilter(filterRequest);
		
		log.info("#############################");
		
		return logs.stream()
		        .map(log -> logConverter.setLogDTO(log, false)) // log_jd 처리 제외
		        .collect(Collectors.toList());
	}

}
