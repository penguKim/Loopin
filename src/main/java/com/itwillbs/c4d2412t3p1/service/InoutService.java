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
import com.itwillbs.c4d2412t3p1.domain.OrderDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.entity.Contract;
import com.itwillbs.c4d2412t3p1.entity.Inout;
import com.itwillbs.c4d2412t3p1.entity.InoutPK;
import com.itwillbs.c4d2412t3p1.entity.Order;
import com.itwillbs.c4d2412t3p1.entity.OrderDetail;
import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.entity.StockPK;
import com.itwillbs.c4d2412t3p1.mapper.InoutMapper;
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
	public List<InoutDTO> select_INOUT_CONTRACTDETAIL(String contractCd) {
		return inoutMapper.select_INOUT_CONTRACTDETAIL(contractCd);
	}


	// 입출고 등록
	@Transactional
	public void insert_INOUT_ORDER(InoutDTO inoutDTO) {

	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
        
	    // 입출고 등록
//        Inout inout = inoutRepository.findById(new InoutPK(
//        		inoutDTO.getInout_dt(), inoutDTO.getInout_iw(), 
//        		inoutDTO.getInout_ow(), inoutDTO.getItem_cd()))
//        		.map(existing -> {
//        			inoutDTO.setInout_uu(regUser);
//        			inoutDTO.setInout_ud(time);
//        			return Inout.setInout(inoutDTO);
//        		})
//        		.orElseGet(() -> {
//        			inoutDTO.setInout_ru(regUser);
//        			inoutDTO.setInout_rd(time);
//        			return Inout.setInout(inoutDTO);
//        		});
//        
//         inoutRepository.save(inout);
        
        // 재고 수정
        // 출발/도착 창고나 구역이 다를경우 출발창고의 재고 수정
        if(!inoutDTO.getInout_iw().equals(inoutDTO.getInout_ow()) || 
        		!inoutDTO.getInout_ia().equals(inoutDTO.getInout_oa())) {
        	System.out.println("출발재고 수정 시작!!!!!!!!!!!!!!!!!!!!!!!!!!");
        	updateInWarehouseStock(inoutDTO);
        }
        
        // 도착창고 재고 수정
        updateOutWarehouseStock(inoutDTO);
        
        // 발주내역
        String datePart = inoutDTO.getInout_dt().substring(0, 10);
        String orderCd = inoutDTO.getInout_co();
        inoutMapper.updateOrderDetail(inoutDTO.getItem_cd(), orderCd, datePart);		
        
        Order order = orderRepository.findById(orderCd).orElseThrow(() -> 
        		new IllegalArgumentException("해당 계약을 찾을 수 없습니다: " + orderCd));
        order.setOrder_ed(datePart);
        order.setOrder_mf(regUser);
        order.setOrder_md(time);
        
        int orderDetailCount = inoutMapper.select_ORDERDETAIL_count(orderCd);
        if(orderDetailCount == 0) {
        	order.setOrder_st("완료");
        }
        orderRepository.save(order);
        
	}

	// 수주건 인서트
	public void insert_INOUT_CONTRACT(InoutDTO inoutDTO) {
        // 재고 수정
        // 출발/도착 창고나 구역이 다를경우 출발창고의 재고 수정
        if(!inoutDTO.getInout_iw().equals(inoutDTO.getInout_ow()) || 
        		!inoutDTO.getInout_ia().equals(inoutDTO.getInout_oa())) {
        	System.out.println("출발재고 수정 시작!!!!!!!!!!!!!!!!!!!!!!!!!!");
        	updateInWarehouseStock(inoutDTO);
        }
        
        // 도착창고 재고 수정
        updateOutWarehouseStock(inoutDTO);
	    
	}


	public void updateInWarehouseStock(InoutDTO inoutDTO) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
    	int withdrawalQty = inoutDTO.getInout_nn();
    	List<Stock> stockList = stockRepository.findStocksForDeduction(
    			inoutDTO.getItem_cd(), inoutDTO.getInout_iw(), inoutDTO.getInout_ia());
    	if(stockList == null || stockList.size() == 0) {
    		throw new RuntimeException("출발창고에 재고가 없습니다."); 
    	}
    	System.out.println("창고리스트 수 : " + stockList.size());
    	for (Stock stock : stockList) {
    		if (withdrawalQty <= 0) {  
    			break;
    		}
    		int available = stock.getStock_aq();
    		if (available > 0) {
    			int deduction = Math.min(available, withdrawalQty);
    			stock.setStock_uu(regUser);
    			stock.setStock_ud(time);
    			stock.setStock_aq(available - deduction);
    			withdrawalQty -= deduction;
    			System.out.println(stock.getWarehouse_cd() 
    					+  "창고의 " + stock.getWarearea_cd() + "에 저장되는 수량 : " + stock.getStock_aq());
    			
    			stockRepository.save(stock);
    		
	        	// 해당 재고건에 대한 입출고 내역도 저장되어야함
	        	// 출고건
	        	Inout inout = new Inout();
	        	inout.setInout_dt(inoutDTO.getInout_dt()); // 일시
	        	inout.setInout_iw(stock.getWarehouse_cd()); // 출발창고
	        	inout.setInout_ia(stock.getWarearea_cd()); // 출발구역
	        	inout.setInout_ow(inoutDTO.getInout_ow()); // 도착창고
	        	inout.setInout_oa(inoutDTO.getInout_oa()); // 도착구역
	        	inout.setItem_cd(inoutDTO.getItem_cd()); // 제품코드
	        	inout.setInout_co(inoutDTO.getInout_co()); // 수발주코드
	        	inout.setInout_tp(inoutDTO.getInout_tp()); // 수발주구분
	        	inout.setLot_cd(inoutDTO.getLot_cd()); // 로트번호
	        	inout.setProcess_cd(inoutDTO.getProcess_cd()); // 공정코드
	        	inout.setInout_nn(inoutDTO.getInout_nn()); // 갯수
	        	inout.setInout_in(inoutDTO.getInout_in()); // 이전 갯수
	        	inout.setInout_fn(inoutDTO.getInout_fn()); // 불량 갯수
	        	inout.setInout_io("O"); // 입출고구분
	        	inout.setEmployee_cd(inoutDTO.getEmployee_cd());
	        	inout.setInout_ru(regUser);
	        	inout.setInout_rd(time);
	        	System.out.println("출거저장!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	        	System.out.println("출발창고 : " + inout.getInout_iw());
	        	System.out.println("출발구역 : " + inout.getInout_ia());
	        	System.out.println("도착창고 : " + inout.getInout_ow());
	        	System.out.println("도착구역 : " + inout.getInout_oa());
	        	inoutRepository.save(inout);
	        	
	        	// 입고건
	        	Inout inout2 = new Inout();
	        	inout2.setInout_dt(inoutDTO.getInout_dt()); // 일시
	        	inout2.setInout_iw(inoutDTO.getInout_ow()); // 출발창고
	        	inout2.setInout_ia(inoutDTO.getInout_oa()); // 출발구역
	        	inout2.setInout_ow(stock.getWarehouse_cd()); // 도착창고
	        	inout2.setInout_oa(stock.getWarearea_cd()); // 도착구역
	        	inout2.setItem_cd(inoutDTO.getItem_cd()); // 제품코드
	        	inout2.setInout_co(inoutDTO.getInout_co()); // 수발주코드
	        	inout2.setInout_tp(inoutDTO.getInout_tp()); // 수발주구분
	        	inout2.setLot_cd(inoutDTO.getLot_cd()); // 로트번호
	        	inout2.setProcess_cd(inoutDTO.getProcess_cd()); // 공정코드
	        	inout2.setInout_nn(inoutDTO.getInout_nn()); // 갯수
	        	inout2.setInout_in(inoutDTO.getInout_in()); // 이전 갯수
	        	inout2.setInout_fn(inoutDTO.getInout_fn()); // 불량 갯수
	        	inout2.setInout_io("I"); // 입출고구분
	        	inout2.setEmployee_cd(inoutDTO.getEmployee_cd());
	        	inout2.setInout_ru(regUser);
	        	inout2.setInout_rd(time);
	        	System.out.println("입고저장!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	        	System.out.println("출발창고 : " + inout2.getInout_iw());
	        	System.out.println("출발구역 : " + inout2.getInout_ia());
	        	System.out.println("도착창고 : " + inout2.getInout_ow());
	        	System.out.println("도착구역 : " + inout2.getInout_oa());
	        	inoutRepository.save(inout2);
    		}
    	}
   	 
    	if (withdrawalQty > 0) {
    		throw new RuntimeException("재고가 부족합니다."); 
    	}
    	

	}
	
	public void updateOutWarehouseStock(InoutDTO inoutDTO) {
		String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		int additionQty = inoutDTO.getInout_nn();
		Stock destStock = stockRepository
				.findById(new StockPK(inoutDTO.getItem_cd(), inoutDTO.getInout_ow(), inoutDTO.getInout_oa()))
				.map(existing -> {
					existing.setStock_uu(regUser);
					existing.setStock_ud(time);
					return existing;
				}).orElseGet(() -> {
					Stock newStock = new Stock();
					newStock.setItem_cd(inoutDTO.getItem_cd());
					newStock.setWarehouse_cd(inoutDTO.getInout_ow());
					newStock.setWarearea_cd(inoutDTO.getInout_oa());
					newStock.setStock_aq(0);
					newStock.setStock_ru(regUser);
					newStock.setStock_rd(time);
					return newStock;
				});
		destStock.setStock_aq(destStock.getStock_aq() + additionQty);
		stockRepository.save(destStock);

		Inout inout = inoutRepository.findById(new InoutPK(inoutDTO.getInout_dt(), inoutDTO.getInout_iw(),
				inoutDTO.getInout_ow(), inoutDTO.getItem_cd(), inoutDTO.getInout_io()))
				.map(existing -> {
					inoutDTO.setInout_uu(regUser);
					inoutDTO.setInout_ud(time);
					return Inout.setInout(inoutDTO);
				}).orElseGet(() -> {
					inoutDTO.setInout_ru(regUser);
					inoutDTO.setInout_rd(time);
					return Inout.setInout(inoutDTO);
				});

		inoutRepository.save(inout);
        
	}

}
