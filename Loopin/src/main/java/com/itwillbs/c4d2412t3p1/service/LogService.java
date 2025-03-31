package com.itwillbs.c4d2412t3p1.service;

import java.time.LocalDateTime;


import java.util.List;
import java.util.Optional;
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

	public List<LogDTO> selectLogSummaries() {
	    // 요약 로그 데이터 조회 (log_jd는 로드되지 않음)
	    List<Log> logs = logRepository.findAllLogSummaries();
	    log.info("로그 요약 데이터 조회!");
	    // 엔티티를 DTO로 변환 (log_jd 파싱은 필요 없음)
	    return logs.stream()
	            .map(log -> logConverter.setLogDTO(log, false))  // false: log_jd 미포함
	            .collect(Collectors.toList());
	}

	public LogDTO selectLogDetail(String logCd) {
	    // 상세 로그 데이터 조회 (필요 시 log_jd 포함)
	    Optional<Log> optionalLog = logRepository.findDetailedLogById(logCd);
	    if (optionalLog.isPresent()) {
	        Log log = optionalLog.get();
	        LogDTO dto = logConverter.setLogDTO(log, true);
	        
	        // log_jd 파싱 및 변환 (상세 조회 시만 수행)
	        String parsedLogDetails = logParser.parseLogDetails(log.getLog_jd());
	        dto.setParsedLogDetails(parsedLogDetails);
	        return dto;
	    } else {
	        throw new RuntimeException("해당 로그를 찾을 수 없습니다. logCd: " + logCd);
	    }
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
