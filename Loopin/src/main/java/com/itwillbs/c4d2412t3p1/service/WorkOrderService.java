package com.itwillbs.c4d2412t3p1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.entity.Workorder;
import com.itwillbs.c4d2412t3p1.repository.WorkorderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkorderRepository workorderRepository;

    /**
     * Workorder 엔티티 저장
     * - workorder_cd가 없으면 여기서 시퀀스 + 날짜로 생성
     */
    @Transactional
    public Workorder saveWorkorder(Workorder workorder) {
        if (workorder.getWorkorder_cd() == null || workorder.getWorkorder_cd().isBlank()) {
            workorder.setWorkorder_cd(generateWorkOrderCd());
        }
        return workorderRepository.save(workorder);
    }

    /**
     * "WOYYYYMMDD0000" 형식으로 식별자 생성
     *  ex) "WO202502260001"
     */
    private String generateWorkOrderCd() {
        // 1) 시퀀스 값 가져오기 (ex: 1)
        Long seqVal = workorderRepository.getNextSequenceValue(); 

        // 2) 오늘 날짜 (yyyyMMdd)
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 3) zero-padding 4자리 (ex: seqVal=1 -> "0001")
        //   - 필요시 5자리, 6자리 등 늘릴 수 있음
        String seqStr = String.format("%04d", seqVal);

        // 최종 "WO202502260001" 등
        return "WO" + dateStr + seqStr;
    }
}
