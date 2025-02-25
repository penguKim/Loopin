package com.itwillbs.c4d2412t3p1.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.itwillbs.c4d2412t3p1.service.TransferService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferScheduler {

    private final TransferService transferService;
    
//    @PostConstruct
//    public void runOnStartup() {
//        log.info("애플리케이션 시작 시 스케줄러 실행");
//        updateTransfers();
//    }
    
    // 매일 자정 실행: "0 0 0 * * ?" 형식의 cron 표현식
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateTransfers() {
        log.info("스케줄러 실행 시작: TRANSFER 테이블 업데이트");

        try {
            transferService.updatePendingTransfers(); // Service 호출
            log.info("스케줄러 작업 완료: TRANSFER 테이블 업데이트 성공");
        } catch (Exception e) {
            log.error("스케줄러 작업 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
