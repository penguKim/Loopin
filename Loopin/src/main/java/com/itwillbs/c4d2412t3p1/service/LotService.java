package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Dailyproductplan;
import com.itwillbs.c4d2412t3p1.entity.DailyproductplanPK;
import com.itwillbs.c4d2412t3p1.entity.Equipment;
import com.itwillbs.c4d2412t3p1.entity.Lot;
import com.itwillbs.c4d2412t3p1.entity.LotPK;
import com.itwillbs.c4d2412t3p1.entity.Process;
import com.itwillbs.c4d2412t3p1.entity.Productplan;
import com.itwillbs.c4d2412t3p1.mapper.EquipmentMapper;
import com.itwillbs.c4d2412t3p1.mapper.LotMapper;
import com.itwillbs.c4d2412t3p1.repository.DailyproductplanRepository;
import com.itwillbs.c4d2412t3p1.repository.EquipmentRepository;
import com.itwillbs.c4d2412t3p1.repository.LotRepository;
import com.itwillbs.c4d2412t3p1.repository.ProcessRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class LotService {
	
	private final UtilService util;
	private final LotMapper lotMapper;
	private final DailyproductplanRepository dailyProductPlanRepository;
	private final LotRepository lotRepository;
	
	// 제품 조회
	public List<Map<String, Object>> select_LOT_list(Map<String, Object> params) {
		return lotMapper.select_LOT_list(params);
	}
	
	
//	public void getDailyPlansByDate() {
//		Timestamp productionDate = new Timestamp(System.currentTimeMillis());
//		List<Dailyproductplan> dailyPlans = dailyProductPlanRepository.findByProductionDate(productionDate);
//
//		for (Dailyproductplan plan : dailyPlans) {
//			DailyproductplanPK id = plan.getId();
////			 Optional<Lot> existingLot = lotRepository.findByProductCdAndProductCrAndProductSz(
////	                    plan.getProductCd(), plan.getProductCr(), plan.getProductSz());
////
////	            String lotCd;
////	            if (existingLot.isPresent()) {
////	                lotCd = existingLot.get().getLotCd();  // 기존 로트번호 사용
////	            } else {
////	                // 2️⃣ 기존 로트번호가 없으면 새로운 로트번호 생성
////	                int nextSeq = lotInfoRepository.countByProductCd(plan.getProductCd()) + 1;
////	                lotCd = plan.getProductCd() + "-" + plan.getProductionDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
////	                        + "-" + String.format("%03d", nextSeq);
////	            }
//            String lotCd = id.getProduct_cd() + "-" + id.getDailyproductplan_sd() + "-" + String.format("%03d");
//
//            LotPK lotPK = new LotPK(lotCd, id.getProcess_cd());
//            log.info("lotCd"+lotCd);
//            Lot lot = new Lot(lotPK, id.getProduct_cd(), id.getContract_cd(), id.getProduct_cr(), id.getProduct_sz(), plan.getDailyproductplan_js(), productionDate);
//            log.info("lot"+lot);
//            lotRepository.save(lot);
//        }
//    }
	
	public List<Map<String, Object>> select_PROCESS_list() {
	    return lotMapper.select_PROCESS_list();
	}


	public Map<String, Object> select_LOT_json(Map<String, Object> params) {
		return lotMapper.select_LOT_json(params);
	}


	public List<Map<String, Object>> select_RESULT_list(Map<String, Object> params) {
		return lotMapper.select_RESULT_list(params);
	}
	
}
