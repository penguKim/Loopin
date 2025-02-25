package com.itwillbs.c4d2412t3p1.config;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.itwillbs.c4d2412t3p1.entity.Dailyproductplan;
import com.itwillbs.c4d2412t3p1.service.ApprovalService;
import com.itwillbs.c4d2412t3p1.service.LotService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LotScheduler {

    @Autowired
    private LotService lotService;

//    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시에 실행
//    public void generateLotInfo() {
//        lotService.getDailyPlansByDate();
//        
//    }
}
