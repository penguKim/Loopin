package com.itwillbs.c4d2412t3p1.logging;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Log;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Component
@lombok.extern.java.Log
public class LogConverter {

    private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    // Log → LogDTO 변환
    public LogDTO setLogDTO(Log log) {
        LogDTO logDTO = new LogDTO();
        logDTO.setLog_cd(log.getLog_cd());
        logDTO.setLog_sj(log.getLog_sj());
        logDTO.setLog_ju(log.getLog_ju());
        logDTO.setLog_od(log.getLog_od());
        logDTO.setLog_oi(log.getLog_oi());
        logDTO.setLog_bj(log.getLog_bj());
        logDTO.setEmployee_id(log.getEmployee() != null ? log.getEmployee().getEmployee_id() : null);

        // JSON → Map 변환
        if (log.getLog_jd() != null) {
            try {
                Map<String, Object> jsonMap = objectMapper.readValue(log.getLog_jd(), Map.class);
                logDTO.setLog_jdMap(jsonMap);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("log_jd JSON parsing 중 오류: " + e.getMessage(), e);
            }
        }

        return logDTO;
    }

    // LogDTO → Log 변환
    public Log toEntity(LogDTO logDTO) {
        Log log = new Log();
        log.setLog_cd(logDTO.getLog_cd());
        log.setLog_sj(logDTO.getLog_sj());
        log.setLog_ju(logDTO.getLog_ju());
        log.setLog_od(logDTO.getLog_od());
        log.setLog_oi(logDTO.getLog_oi());
        log.setLog_bj(logDTO.getLog_bj());
        log.setSequenceValue(logDTO.getSequenceValue()); // 시퀀스 값 설정

        // Employee 설정
//        Employee employee = new Employee();
//        employee.setEmployee_cd(Long.parseLong(logDTO.getEmployee_id()));
//        log.setEmployee(employee);
        
        if (logDTO.getEmployee_id() != null) {
            Employee employee = new Employee();
            employee.setEmployee_cd(logDTO.getEmployee_id()); // NumberFormatException 가능
            log.setEmployee(employee);
        } else {
            logger.info("임시로 employee_cd NULL 사용");
        }
 
        // Map → JSON 변환
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
