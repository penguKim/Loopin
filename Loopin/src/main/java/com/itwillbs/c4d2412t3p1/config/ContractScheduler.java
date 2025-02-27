package com.itwillbs.c4d2412t3p1.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itwillbs.c4d2412t3p1.service.BusinessService;
import com.itwillbs.c4d2412t3p1.service.TransferService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractScheduler {
	
   private final BusinessService businessService;

    // 하루 마다 실행 (자정 실행)
    @Scheduled(cron = "0 0 0 * * *")  
    public void updateContractStatus() {
        log.info("스케쥴러 수주 상태 업데이트 실행...");

        try {
            businessService.updateContractStatus(); // Service 호출
            log.info("스케줄러 수주 상태 업데이트 완료!");
        } catch (Exception e) {
            log.error("스케줄러 오류 수주 상태 업데이트 실패: {}", e.getMessage(), e);
        }
    }
}
