package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.InoutWarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.entity.Lot;
import com.itwillbs.c4d2412t3p1.entity.LotPK;
import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.entity.Workorder;
import com.itwillbs.c4d2412t3p1.repository.BomProcessRepository;
import com.itwillbs.c4d2412t3p1.repository.BomRepository;
import com.itwillbs.c4d2412t3p1.repository.LotRepository;
import com.itwillbs.c4d2412t3p1.repository.MaterialRepository;
import com.itwillbs.c4d2412t3p1.repository.StockRepository;
import com.itwillbs.c4d2412t3p1.repository.WorkorderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WorkorderRepository workorderRepository;
    private final LotRepository lotRepository;
    private final BomProcessRepository bomProcessRepository;
    private final BomRepository bomRepository;
    private final MaterialRepository materialRepository;
    private final StockRepository stockRepository;
    private final InoutService inoutService;
    
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
    
 // 작업시작 처리: UPPER 공정의 입력 자재 투입(출고)
    @Transactional
    public void process_workorder_start(Workorder workorder) {
        try {
            // LOT 조회 (복합키 사용)
            LotPK lotPk = new LotPK(workorder.getLot_cd(), workorder.getProcess_cd());
            Lot lot = lotRepository.findById(lotPk)
                    .orElseThrow(() -> new RuntimeException("LOT 정보 없음: " 
                            + workorder.getLot_cd() + ", " + workorder.getProcess_cd()));
            
            // BOMPROCESS에서 현재 공정(UPPER)의 행 조회
            Optional<BomProcess> bpOpt = bomProcessRepository.findByProductCd(lot.getProduct_cd())
                    .stream()
                    .filter(bp -> bp.getProcess_cd().equals(lot.getId().getProcess_cd()))
                    .findFirst();
            if (!bpOpt.isPresent())
                throw new RuntimeException("BOMPROCESS 정보 없음: " + lot.getProduct_cd() + ", " + lot.getId().getProcess_cd());
            BomProcess bp = bpOpt.get();
            
            // UPPER 공정의 투입 자재는 BOM 테이블에서 finished product(lot.getProduct_cd())와 연관된 행 중,
            // product_cd가 bp.getBomprocess_cd()인 행의 bom_cd를 사용 (없으면 BOMPROCESS의 코드를 그대로 사용)
            Optional<Bom> bomOpt = bomRepository.findByBomProductCdAndProductCd(lot.getProduct_cd(), bp.getBomprocess_cd());
            String inputMaterialCode = bomOpt.isPresent() ? bomOpt.get().getBom_cd() : bp.getBomprocess_cd();
            
            // 출고용 InoutDTO 구성: 투입 자재 코드(inputMaterialCode) 사용
            InoutDTO outboundDTO = new InoutDTO();
            outboundDTO.setInout_dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            outboundDTO.setItem_cd(inputMaterialCode);
            outboundDTO.setLot_cd(lot.getId().getLot_cd());
            outboundDTO.setProcess_cd(lot.getId().getProcess_cd());
            outboundDTO.setInout_nn(lot.getDailyproductplan_js());
            outboundDTO.setInout_io("O");
            
            // STOCK에서 해당 투입 자재의 재고 정보를 조회하여, 창고 및 구역 정보를 동적으로 할당
            List<Stock> stockList = stockRepository.findByItem_cd(outboundDTO.getItem_cd());
            if (stockList.isEmpty())
                throw new RuntimeException("출고 재고 정보가 없습니다: " + outboundDTO.getItem_cd());
            Stock sourceStock = stockList.get(0);
            InoutWarehouseDTO outboundWarehouse = InoutWarehouseDTO.builder()
                    .ow_warehouse_cd(sourceStock.getWarehouse_cd())
                    .ow_warearea_cd(sourceStock.getWarearea_cd())
                    .ow_inout_nn(outboundDTO.getInout_nn())
                    .build();

            Timestamp nowTimestamp = Timestamp.valueOf(LocalDateTime.now());
            String regUser = "system";

            inoutService.setOutboundStock(outboundDTO, outboundWarehouse, regUser, nowTimestamp);
            workorder.setWorkorder_st("진행중");
            workorderRepository.save(workorder);
        } catch (Exception e) {
            log.severe("작업시작 처리 실패 - Workorder: " + workorder.getWorkorder_cd() + " / " + e.getMessage());
        }
    }
    
    // 작업종료 처리: UPPER 공정의 산출 결과만 입고 처리
    @Transactional
    public void process_workorder_end(Workorder workorder) {
        try {
            LotPK lotPk = new LotPK(workorder.getLot_cd(), workorder.getProcess_cd());
            Lot lot = lotRepository.findById(lotPk)
                    .orElseThrow(() -> new RuntimeException("LOT 정보 없음: " 
                            + workorder.getLot_cd() + ", " + workorder.getProcess_cd()));
            
            // BOMPROCESS에서 현재 공정(UPPER)의 정보를 조회
            Optional<BomProcess> bpOpt = bomProcessRepository.findByProductCd(lot.getProduct_cd())
                    .stream()
                    .filter(bp -> bp.getProcess_cd().equals(lot.getId().getProcess_cd()))
                    .findFirst();
            if (!bpOpt.isPresent())
                throw new RuntimeException("BOMPROCESS 정보 없음: " + lot.getProduct_cd() + ", " + lot.getId().getProcess_cd());
            BomProcess bp = bpOpt.get();
            
            // 생산비율과 불량률 적용하여 산출량 계산
            int productionFactor = Integer.parseInt(bp.getBomprocess_rt());  // 예: 2
            int defectRate = Integer.parseInt(bp.getBomprocess_er());          // 예: 3 (%)
            int workQty = lot.getDailyproductplan_js();
            int totalProduction = workQty * productionFactor;
            int defectiveQty = (int) Math.round(totalProduction * defectRate / 100.0);
            int goodQty = totalProduction - defectiveQty;
            
            // 작업종료 시, 입고할 산출물은 BOMPROCESS의 bomprocess_cd 그대로 사용 (예: "UP002")
            String outputCode = bp.getBomprocess_cd();
            // ※ 기존에는 BOM 테이블 조회를 통해 변경했으나, 여기서는 산출물이 바로 UP002가 되어야 하므로 변환하지 않음.
            
            InoutDTO inboundDTO = new InoutDTO();
            inboundDTO.setInout_dt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            inboundDTO.setItem_cd(outputCode);
            inboundDTO.setLot_cd(lot.getId().getLot_cd());
            inboundDTO.setProcess_cd(lot.getId().getProcess_cd());
            inboundDTO.setInout_nn(goodQty);
            inboundDTO.setInout_fn(defectiveQty);
            inboundDTO.setInout_io("I");
            
            // STOCK에서 해당 산출 자재(UP002)의 재고 정보를 조회하여, 입고 창고 정보를 동적으로 할당
            List<Stock> stockList = stockRepository.findByItem_cd(inboundDTO.getItem_cd());
            if (stockList.isEmpty())
                throw new RuntimeException("입고 재고 정보가 없습니다: " + inboundDTO.getItem_cd());
            Stock destStock = stockList.get(0);
            InoutWarehouseDTO inboundWarehouse = InoutWarehouseDTO.builder()
                    .iw_warehouse_cd(destStock.getWarehouse_cd())
                    .iw_warearea_cd(destStock.getWarearea_cd())
                    .iw_inout_nn(goodQty)
                    .build();
            
            Timestamp nowTimestamp = Timestamp.valueOf(LocalDateTime.now());
            String regUser = "system";
            
            inoutService.setInboundStock(inboundDTO, inboundWarehouse, regUser, nowTimestamp);
            workorder.setWorkorder_st("완료");
            workorderRepository.save(workorder);
        } catch (Exception e) {
            log.severe("작업종료 처리 실패 - Workorder: " + workorder.getWorkorder_cd() + " / " + e.getMessage());
        }
    }
}
