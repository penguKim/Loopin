package com.itwillbs.c4d2412t3p1.logging;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Log;
import com.itwillbs.c4d2412t3p1.repository.LogRepository;
import com.itwillbs.c4d2412t3p1.repository.TransferRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Component
@lombok.extern.java.Log
public class LogConverter {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(LogConverter.class);

    /**
     * Log 엔티티를 LogDTO로 변환.
     * includeLogJd가 true이면 log_jd 필드를 JSON으로 파싱하여 LogDTO에 설정한다.
     */
    public LogDTO setLogDTO(Log log, boolean includeLogJd) {
        LogDTO logDTO = new LogDTO();
        logDTO.setLog_cd(log.getLog_cd());
        logDTO.setLog_sj(log.getLog_sj());
        logDTO.setLog_ju(log.getLog_ju());
        logDTO.setLog_od(log.getLog_od());
        logDTO.setLog_oi(log.getLog_oi());
        logDTO.setLog_bj(log.getLog_bj());
        logDTO.setSequenceValue(log.getSequenceValue());
        
        // Employee 정보 매핑 (없으면 경고 출력)
        if (log.getEmployee() != null) {
            logDTO.setEmployee_cd(log.getEmployee().getEmployee_cd());
            logDTO.setEmployee_id(log.getEmployee().getEmployee_id());
        } else {
            logger.warn("Log 엔티티에서 Employee 정보가 없습니다. log_cd: {}", log.getLog_cd());
        }
        
        // log_jd 필드를 필요할 때만 처리 (예: 상세 조회 시)
        if (includeLogJd && log.getLog_jd() != null) {
            try {
                Map<String, Object> jsonMap = objectMapper.readValue(log.getLog_jd(), Map.class);
                logDTO.setLog_jdMap(jsonMap);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("log_jd JSON 파싱 중 오류: " + e.getMessage(), e);
            }
        }
        
        return logDTO;
    }

    /**
     * LogDTO를 Log 엔티티로 변환.
     * log_jdMap이 존재할 경우에만 log_jd 필드를 JSON 문자열로 변환하여 설정한다.
     */
    public Log toEntity(LogDTO logDTO) {
        Log log = new Log();
        log.setLog_cd(logDTO.getLog_cd());
        log.setLog_sj(logDTO.getLog_sj());
        log.setLog_ju(logDTO.getLog_ju());
        log.setLog_od(logDTO.getLog_od());
        log.setLog_oi(logDTO.getLog_oi());
        log.setLog_bj(logDTO.getLog_bj());
        log.setSequenceValue(logDTO.getSequenceValue());

        // Employee 설정: employee_cd를 통해 employee_id 조회 후 설정
        if (logDTO.getEmployee_cd() != null) {
            String employeeId = logRepository.findEmployeeIdByEmployeeCd(logDTO.getEmployee_cd());
            if (employeeId != null) {
                Employee employee = new Employee();
                employee.setEmployee_id(employeeId);
                employee.setEmployee_cd(logDTO.getEmployee_cd());
                log.setEmployee(employee);
            } else {
                throw new IllegalArgumentException("employee_cd에 해당하는 Employee가 없습니다: " + logDTO.getEmployee_cd());
            }
        } else {
            logger.warn("logDTO에 employee_cd가 없습니다. Log의 employee는 null로 설정됩니다.");
        }

        // log_jdMap이 있다면 JSON 문자열로 변환하여 log_jd 필드에 설정
        if (logDTO.getLog_jdMap() != null) {
            try {
                String jsonString = objectMapper.writeValueAsString(logDTO.getLog_jdMap());
                log.setLog_jd(jsonString);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("log_jdMap JSON 변환 오류: " + e.getMessage(), e);
            }
        }
        return log;
    }
}
