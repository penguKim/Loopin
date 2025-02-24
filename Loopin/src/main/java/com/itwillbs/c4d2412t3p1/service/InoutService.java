package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.InoutRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.InoutWarehouseDTO;
import com.itwillbs.c4d2412t3p1.domain.OrderDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Contract;
import com.itwillbs.c4d2412t3p1.entity.Inout;
import com.itwillbs.c4d2412t3p1.entity.Inout;
import com.itwillbs.c4d2412t3p1.entity.InoutPK;
import com.itwillbs.c4d2412t3p1.entity.Order;
import com.itwillbs.c4d2412t3p1.entity.OrderDetail;
import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.entity.StockPK;
import com.itwillbs.c4d2412t3p1.mapper.InoutMapper;
import com.itwillbs.c4d2412t3p1.repository.InoutRepository;
import com.itwillbs.c4d2412t3p1.repository.InoutRepository;
import com.itwillbs.c4d2412t3p1.repository.OrderRepository;
import com.itwillbs.c4d2412t3p1.repository.StockRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class InoutService {
	
	private final UtilService util;
	private final InoutMapper inoutMapper;
	private final InoutRepository inoutRepository;
	private final StockRepository stockRepository;
	private final OrderRepository orderRepository;
	private final ObjectMapper mapper;

	// 입출고 조회
	public List<InoutDTO> select_INOUT_list() {
		return inoutMapper.select_INOUT_list();
	}

	// 발주 조회
	public List<InoutDTO> select_INOUT_ORDER() {
		return inoutMapper.select_INOUT_ORDER();
	}

	// 발주품목 조회
	public List<InoutDTO> select_INOUT_ORDERDETAIL(String orderCd) {
		return inoutMapper.select_INOUT_ORDERDETAIL(orderCd);
	}
	
	// 수주 조회
	public List<InoutDTO> select_INOUT_CONTRACT() {
		return inoutMapper.select_INOUT_CONTRACT();
	}
	
	// 수주품목 조회
	public List<InoutDTO> select_INOUT_CONTRACTDETAIL(InoutRequestDTO inoutRequest) {
		String contractCd = inoutRequest.getContract_cd(); // 수주번호
		String productCd = inoutRequest.getProduct_cd(); // 제조품목코드
		String processCd = inoutRequest.getProcess_cd(); // 공정코드
		String itemfullCd = inoutRequest.getItem_cd(); // 완성품코드
		String itemCd = itemfullCd.substring(0, itemfullCd.indexOf("-"));
		String itemSz = inoutRequest.getItem_sz();
		String itemCr = inoutRequest.getItem_cr();
		
		return inoutMapper.select_INOUT_CONTRACTDETAIL(contractCd, productCd, processCd, itemfullCd, itemCd, itemSz, itemCr);
	}
	



	// 출발창고 조회
	public List<WareareaDTO> select_INOUT_OW(String item_cd) {
		return inoutMapper.select_INOUT_OW(item_cd);
	}
//	public List<WareareaDTO> select_INOUT_IW(String item_cd, List<WareareaDTO> list) {
//		return inoutMapper.select_INOUT_IW(item_cd, list);
//	}

	// 도착창고 조회
	public List<WareareaDTO> select_INOUT_IW(String item_cd) {
		return inoutMapper.select_INOUT_IW(item_cd);
	}

	
	
	
	
	
	@Transactional
	public void insert_INOUT_ORDER(InoutDTO inoutDTO, List<InoutWarehouseDTO> iwList) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());

	    // 1. 도착창고 재고 추가 처리
	    for (InoutWarehouseDTO warehouse : iwList) {
            handleInbound(inoutDTO, warehouse, regUser, time);
	    }
	    
	    // 2. 발주내역 업데이트 (수주건의 경우에도 발주 상태를 업데이트해야 할 경우)
	    String datePart = inoutDTO.getInout_dt().substring(0, 10);
	    String orderCd = inoutDTO.getInout_co();

	    // 발주 상세 업데이트
	    inoutMapper.updateOrderDetail(inoutDTO.getItem_cd(), orderCd, datePart);

	    Order order = orderRepository.findById(orderCd).orElseThrow(() ->
	            new IllegalArgumentException("해당 계약을 찾을 수 없습니다: " + orderCd));
	    order.setOrder_ed(datePart);
	    order.setOrder_mf(regUser);
	    order.setOrder_md(time);

	    int orderDetailCount = inoutMapper.select_ORDERDETAIL_count(orderCd);
	    if (orderDetailCount == 0) {
	        order.setOrder_st("완료");
	    }
	    orderRepository.save(order);
	}
	
	@Transactional
	public void insert_INOUT_CONTRACT4(InoutDTO inoutDTO, List<InoutWarehouseDTO> iwList) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    String gubun = inoutDTO.getInout_io();
	    System.out.println("입출고 데이터 : " + inoutDTO.toString());

	    for (InoutWarehouseDTO warehouse : iwList) {
			System.out.println("창고 : " + warehouse.toString());
	        // 출고 처리
	        if ("A".equals(gubun) || "O".equals(gubun)) {
	            handleOutbound(inoutDTO, warehouse, regUser, time);
	        }
	        // 입고 처리
	        if ("A".equals(gubun) || "I".equals(gubun)) {
	            handleInbound(inoutDTO, warehouse, regUser, time);
	        }
	    }
	}

	// 출고 처리 로직
	private void handleOutbound(InoutDTO inoutDTO, InoutWarehouseDTO warehouse, String regUser, Timestamp time) {
	    System.out.println("출고할거야!!!!!!!!!!!!!!!!!!!");

	    int withdrawalQty = warehouse.getOw_inout_nn(); // 출발창고에서 차감할 수량
	    Stock sourceStock = stockRepository.findById(
	            new StockPK(inoutDTO.getItem_cd(), warehouse.getOw_warehouse_cd(), warehouse.getOw_warearea_cd()))
	            .orElseThrow(() -> new RuntimeException("출발창고 재고가 없습니다: "
	                    + warehouse.getOw_warehouse_cd() + ", " + warehouse.getOw_warearea_cd()));

	    if (sourceStock.getStock_aq() < withdrawalQty) {
	        throw new RuntimeException("출발창고 재고가 부족합니다: 창고="
	                + warehouse.getOw_warehouse_cd() + ", 구역=" + warehouse.getOw_warearea_cd());
	    }
	    
	    // 재고 차감
	    int stockAq = sourceStock.getStock_aq(); // 출고수량
	    sourceStock.setStock_aq(stockAq - withdrawalQty);
	    sourceStock.setStock_uu(regUser);
	    sourceStock.setStock_ud(time);
	    stockRepository.save(sourceStock);

	    System.out.println("출고 저장: 출고창고=" + warehouse.getOw_warehouse_cd() + ", 출고구역=" + warehouse.getOw_warearea_cd() +
	            ", 수량=" + withdrawalQty);

	    // 출고 내역 등록
	    Inout outRecord = Inout.createOutRecord(
	            inoutDTO,
	            warehouse.getOw_warehouse_cd(), // 출발창고
	            warehouse.getOw_warearea_cd(), // 출발구역
	            withdrawalQty, // 수량
	            stockAq, // 이전 총수량
	            warehouse.getOw_inout_fn(), // 불량률
	            regUser,
	            time
	    );
	    inoutRepository.save(outRecord);
	}

	// 입고 처리 로직
	private void handleInbound(InoutDTO inoutDTO, InoutWarehouseDTO warehouse, String regUser, Timestamp time) {
		System.out.println("입고할거야!!!!!!!!!!!!!!!!!!!");
		
	    int additionQty = warehouse.getIw_inout_nn();  // 도착창고에서 추가할 수량
		System.out.println("입고 저장: 입고창고=" + warehouse.getIw_warehouse_cd() + ", 입고구역=" + warehouse.getIw_warearea_cd() + 
				", 수량=" + additionQty);
	    Stock destStock = stockRepository.findById(
            new StockPK(inoutDTO.getItem_cd(), warehouse.getIw_warehouse_cd(), warehouse.getIw_warearea_cd()))
            .map(existing -> {
                existing.setStock_uu(regUser);
                existing.setStock_ud(time);
                return existing;
            })
            .orElseGet(() -> {
                Stock newStock = new Stock();
                newStock.setItem_cd(inoutDTO.getItem_cd());
                newStock.setWarehouse_cd(warehouse.getIw_warehouse_cd());
                newStock.setWarearea_cd(warehouse.getIw_warearea_cd());
                newStock.setStock_aq(0);
                String itemGc = inoutDTO.getItem_gc();
                String stockTp = ("PRODUCT".equals(itemGc) || "HALFPRO".equals(itemGc)) ? "Y" : "N";
                newStock.setStock_tp(stockTp);
                newStock.setStock_ru(regUser);
                newStock.setStock_rd(time);
                return newStock;
            });
	    int stockAq = destStock.getStock_aq(); // 입고수량
	    destStock.setStock_aq(stockAq + additionQty);
	    stockRepository.save(destStock);

	    // 입고 내역 등록
	    Inout inRecord = Inout.createInRecord(
	            inoutDTO,
	            warehouse.getIw_warehouse_cd(), // 도착창고
	            warehouse.getIw_warearea_cd(), // 도착구역
	            additionQty, // 수량
	            stockAq, // 이전 총수량
	            warehouse.getIw_inout_fn(), // 불량률
	            regUser,
	            time
	    );
	    inoutRepository.save(inRecord);
	}

	
	
	
	
	
	
	
	
	
	
	// 수주건 로트 조회
	public List<InoutDTO> select_INOUT_LOT(String contract_cd) {
		return inoutMapper.select_INOUT_LOT(contract_cd);
	}
	
}
