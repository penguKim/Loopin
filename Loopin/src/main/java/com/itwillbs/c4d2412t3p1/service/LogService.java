package com.itwillbs.c4d2412t3p1.service;

import java.time.LocalDateTime;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.entity.Log;
import com.itwillbs.c4d2412t3p1.logging.LogConverter;
import com.itwillbs.c4d2412t3p1.logging.LogParser;
import com.itwillbs.c4d2412t3p1.repository.LogRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.LogFilterRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@lombok.extern.java.Log
public class LogService {
	private final LogRepository logRepository;
	private final LogConverter logConverter;
	private final LogParser logParser;
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

	    // 엔티티를 DTO로 변환하며 log_jd를 가공
	    return logs.stream()
	            .map(log -> {
	                LogDTO dto = logConverter.setLogDTO(log, true);
	                
	                // log_jd 파싱 및 변환
	                String parsedLogDetails = logParser.parseLogDetails(log.getLog_jd());
	                dto.setParsedLogDetails(parsedLogDetails); // DTO에 저장
	                
	                return dto;
	            })
	            .collect(Collectors.toList());
	}

	public List<LogDTO> select_FILTERED_LOG(LogFilterRequest filterRequest) {
	    log.info(filterRequest.toString());

	    // 필터 조건에 따라 로그 데이터 조회
	    List<Log> logs = logRepository.findLogsByFilter(filterRequest);

	    log.info("#############################");

	    // 로그 데이터를 DTO로 변환하며 log_jd를 파싱
	    return logs.stream()
	        .map(log -> {
	            // log_jd 파싱 및 변환
	            String parsedLogDetails = logParser.parseLogDetails(log.getLog_jd());

	            // DTO 변환 및 파싱 데이터 추가
	            LogDTO dto = logConverter.setLogDTO(log, true); // log_jd 포함
	            dto.setParsedLogDetails(parsedLogDetails); // 파싱된 접근 데이터 추가

	            return dto;
	        })
	        .collect(Collectors.toList());
	}

}
