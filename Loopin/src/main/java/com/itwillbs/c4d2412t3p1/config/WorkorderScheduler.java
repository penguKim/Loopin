package com.itwillbs.c4d2412t3p1.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.entity.Workorder;
import com.itwillbs.c4d2412t3p1.repository.WorkorderRepository;
import com.itwillbs.c4d2412t3p1.service.WorkOrderService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Component
@RequiredArgsConstructor
public class WorkorderScheduler {

    private final WorkOrderService workOrderService;
    private final WorkorderRepository workorderRepository;
    
    /**
     * 서버 실행 시 한 번 실행
     */
    @PostConstruct
    public void init() {
        log.info("🚀 서버 실행: 작업지시 처리 즉시 실행!");
        process_workorders();
    }
    
    // 매 분마다 실행 – 현재 시간(HH:mm)을 기준으로 처리
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void process_workorders() {
        LocalDateTime now = LocalDateTime.now();
        String currentTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        LocalDate today = now.toLocalDate();

        // 실제 환경에서는 오늘 날짜 및 상태 조건으로 조회
        List<Workorder> workorders = workorderRepository.findAll();

        for (Workorder workorder : workorders) {
            LocalDate workDate = workorder.getDailyproductplan_sd().toLocalDateTime().toLocalDate();
            if (!today.equals(workDate))
                continue;

            String startTime = workorder.getWorkorder_sd().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String endTime = workorder.getWorkorder_ed().toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

            if ("대기".equals(workorder.getWorkorder_st()) && currentTime.equals(startTime)) {
                workOrderService.process_workorder_start(workorder);
            }
            if ("진행중".equals(workorder.getWorkorder_st()) && currentTime.equals(endTime)) {
                workOrderService.process_workorder_end(workorder);
            }
        }
    }
}
