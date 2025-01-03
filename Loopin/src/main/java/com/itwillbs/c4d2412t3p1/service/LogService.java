package com.itwillbs.c4d2412t3p1.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.entity.Log;
import com.itwillbs.c4d2412t3p1.repository.LogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository logRepository;
    private final LogConverter logConverter;

//    연도별 고유 로그 코드 생성
    @Transactional
    public synchronized String generateLogCode() {
        int year = LocalDateTime.now().getYear() % 100; // 두 자리 연도
        Integer currentSequence = logRepository.getCurrentSequenceForYear(year);

        int nextSequence = (currentSequence == null) ? 1 : currentSequence + 1;

        // 시퀀스 업데이트
        logRepository.updateSequenceForYear(year, nextSequence);

        return String.format("%02d-%04d", year, nextSequence);
    }

//    로그 저장
    public void saveLog(LogDTO logDTO) {
        // DTO → 엔티티 변환 후 저장
        Log logEntity = logConverter.toEntity(logDTO);
        logRepository.save(logEntity);
    }

//    모든 로그와 직원 정보를 포함하여 조회
    public List<LogDTO> getLogs() {
        // DB에서 로그와 직원 정보를 함께 조회
        List<Log> logEntities = logRepository.findAllLogsWithEmployee();

        // 엔티티 → DTO 변환
        return logEntities.stream()
                          .map(logConverter::setLogDTO)
                          .collect(Collectors.toList());
    }
}
