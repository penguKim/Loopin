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
import com.itwillbs.c4d2412t3p1.entity.Process;
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
	
	
	public List<Dailyproductplan> getDailyPlansByDate(Timestamp productionDate) {
        return dailyProductPlanRepository.findByProductionDate(productionDate);
    }
	
	public List<Map<String, Object>> select_PROCESS_list() {
	    return lotMapper.select_PROCESS_list();
	}
	
}
