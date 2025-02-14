package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.OrderDTO;
import com.itwillbs.c4d2412t3p1.domain.OrderDetailDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Order;
import com.itwillbs.c4d2412t3p1.entity.OrderDetail;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.OrderRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.OrderFilterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class BusinessService {

	private final OrderRepository orderRepository;

	// 원자재조회
	public List<Map<String, Object>> select_MATERIAL() {

		List<Object[]> result;

		result = orderRepository.select_MATERIAL();

		return result.stream().map(row -> {
			Map<String, Object> material = new HashMap<>();
			material.put("material_cd", row[0]);
			material.put("material_nm", row[1]);
			material.put("material_un", row[2]);

			return material;

		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> search_MATERIAL(String keyword) {

	    List<Object[]> result;

		result = orderRepository.search_MATERIAL(keyword);

		return result.stream().map(row -> {
			Map<String, Object> material = new HashMap<>();
			material.put("material_cd", row[0]);
			material.put("material_nm", row[1]);
			material.put("material_un", row[2]);

	        return material;

	    }).collect(Collectors.toList());
	}
	
	// 발주조회
	public List<Map<String, Object>> select_ORDER() {

		List<Object[]> result;

		result = orderRepository.select_ORDER();

		return result.stream().map(row -> {
			Map<String, Object> order = new HashMap<>();
			order.put("order_cd", row[0]);
			order.put("account_cd", row[1]);
			order.put("order_ps", row[2]);
			order.put("order_sd", row[3]);
			order.put("order_ed", row[4]);
			order.put("order_am", row[5]);
			order.put("order_mn", row[6]);
			order.put("order_st", row[7]);
			order.put("order_rm", row[8]);
			order.put("order_wr", row[9]);
			order.put("order_wd", row[10]);
			order.put("order_mf", row[11]);
			order.put("order_md", row[12]);

			return order;

		}).collect(Collectors.toList());
	}

	// 거래처 조회
	public List<Map<String, Object>> select_ACCOUNT_ORDER() {

		List<Object[]> result;

		result = orderRepository.select_ACCOUNT_ORDER();

		return result.stream().map(row -> {
			Map<String, Object> account = new HashMap<>();
			account.put("account_cd", row[0]);
			account.put("account_nm", row[1]);

			return account;

		}).collect(Collectors.toList());
	}
	
	public List<Map<String, Object>> search_ACCOUNT_ORDER(String keyword) {
		
		List<Object[]> result;
		
		result = orderRepository.search_ACCOUNT_ORDER(keyword);
		
		return result.stream().map(row -> {
			Map<String, Object> account = new HashMap<>();
			account.put("account_cd", row[0]);
			account.put("account_nm", row[1]);
			
			return account;
			
		}).collect(Collectors.toList());
	}
	

	// 담당자 조회
	public List<Map<String, Object>> select_ORDER_PS() {

		List<Object[]> result;

		result = orderRepository.select_ORDER_PS();

		return result.stream().map(row -> {
			Map<String, Object> order_ps = new HashMap<>();
			order_ps.put("employee_cd", row[0]);
			order_ps.put("employee_nm", row[1]);
			order_ps.put("employee_dp", row[2]);
			order_ps.put("employee_gd", row[3]);

			return order_ps;

		}).collect(Collectors.toList());
	}

	
	public List<Map<String, Object>> search_ORDER_PS(String keyword) {
		
		List<Object[]> result;
		
		result = orderRepository.search_ORDER_PS(keyword);
		
		return result.stream().map(row -> {
			Map<String, Object> order_ps = new HashMap<>();
			order_ps.put("employee_cd", row[0]);
			order_ps.put("employee_nm", row[1]);
			order_ps.put("employee_dp", row[2]);
			order_ps.put("employee_gd", row[3]);
			
			return order_ps;
			
		}).collect(Collectors.toList());
	}
	
	// 발주 등록 처리
	@Transactional
	public void insert_ORDER(OrderDTO orderDto, List<OrderDetailDTO> details) throws IOException {

		// 시퀀스를 통해 order_cd 생성 및 헤드 저장
		Long sequenceValue = orderRepository.getNextSequenceValue();
		Order order = Order.createOrder(orderDto, sequenceValue);
		orderRepository.save(order);

		for (OrderDetailDTO detail : details) {
			OrderDetail orderDetail = OrderDetail.builder()
					.order_cd(order.getOrder_cd())
					.material_cd(detail.getMaterial_cd())
					.material_am(detail.getMaterial_am())
					.order_ct(detail.getOrder_ct())
					.order_ed(detail.getOrder_ed())
					.material_un(detail.getMaterial_un())
					.build();

			orderRepository.saveOrderDetail(orderDetail);
		}
	}
	
	
	public void update_ORDER(OrderDTO orderDto, List<OrderDetailDTO> details) throws IOException {
	    // 기존 발주 정보 조회
		Order existingOrder = orderRepository.findById(orderDto.getOrder_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 발주을 찾을 수 없습니다: " + orderDto.getOrder_cd()));

	    //  작성자와 작성일 유지
		orderDto.setOrder_wr(existingOrder.getOrder_wr()); // 작성자 유지
		orderDto.setOrder_wd(existingOrder.getOrder_wd()); // 작성일 유지

	    // 기존 발주 정보 업데이트
	    existingOrder.setAccount_cd(orderDto.getAccount_cd());
	    existingOrder.setOrder_ps(orderDto.getOrder_ps());
	    existingOrder.setOrder_sd(orderDto.getOrder_sd());
	    existingOrder.setOrder_ed(orderDto.getOrder_ed());
	    existingOrder.setOrder_am(orderDto.getOrder_am());
	    existingOrder.setOrder_mn(orderDto.getOrder_mn());
	    existingOrder.setOrder_st(orderDto.getOrder_st());
	    existingOrder.setOrder_rm(orderDto.getOrder_rm());
	    existingOrder.setOrder_wr(orderDto.getOrder_wr());
	    existingOrder.setOrder_wd(orderDto.getOrder_wd());
	    existingOrder.setOrder_mf(orderDto.getOrder_mf());
	    existingOrder.setOrder_md(orderDto.getOrder_md());

	    
	    // 변경된 발주 저장
	    orderRepository.save(existingOrder);
	    
	    // 기존 상세 정보 삭제
	    orderRepository.deleteOrderDetailsByOrderCd(orderDto.getOrder_cd());
	   
	    // 새로운 상세 정보 저장
	    for (OrderDetailDTO detail : details) {
	        OrderDetail orderDetail = OrderDetail.builder()
	            .order_cd(orderDto.getOrder_cd())
	            .material_cd(detail.getMaterial_cd())
	            .material_am(detail.getMaterial_am())
	            .order_ct(detail.getOrder_ct())
	            .order_ed(orderDto.getOrder_ed()) // ORDERDETAIL의 order_ed를 ORDER의 값으로 설정
	            .material_un(detail.getMaterial_un())
	            .build();

	        orderRepository.saveOrderDetail(orderDetail);
	    }

	}
	

	public List<Map<String, Object>> select_ORDERDETAIL(String order_cd) {
		// Repository에서 데이터 조회
		List<Object[]> result = orderRepository.select_ORDERDETAIL(order_cd);

		// Object[] 데이터를 Map<String, Object>로 변환
		return result.stream().map(row -> {
			Map<String, Object> detail = new HashMap<>();
			detail.put("order_cd", row[0]);
			detail.put("material_cd", row[1]);
			detail.put("material_am", row[2]);
			detail.put("order_ct", row[3]);
			detail.put("order_ed", row[4]);
			detail.put("material_un", row[5]);

			return detail;
		}).collect(Collectors.toList());
	}

	public Map<String, Object> getOrderDetails(String orderCd) {
		Map<String, Object> result = new HashMap<>();

		// 발주 정보 조회
		List<Object[]> orderList = orderRepository.findOrderByCd(orderCd);
		
		if (!orderList.isEmpty()) {
			Object[] row = orderList.get(0); // 단일 계약이므로 첫 번째 행 사용
			Map<String, Object> order = new HashMap<>();
			order.put("order_cd", row[0]);
			order.put("account_cd", row[1]);
			order.put("order_ps", row[2]);
			order.put("order_sd", row[3]);
			order.put("order_ed", row[4]);
			order.put("order_am", row[5]);
			order.put("order_mn", row[6]);
			order.put("order_st", row[7]);
			order.put("order_rm", row[8]);
			order.put("order_wr", row[9]);
			order.put("order_wd", row[10]);
			order.put("order_mf", row[11]);
			order.put("order_md", row[12]);

			result.put("order", order);
		}

		// 상세 정보 조회
		List<Object[]> detailList = orderRepository.select_ORDERDETAIL(orderCd);
		List<Map<String, Object>> details = detailList.stream().map(row -> {
			Map<String, Object> detail = new HashMap<>();
			detail.put("order_cd", row[0]);
			detail.put("material_cd", row[1]);
			detail.put("material_am", row[2]);
			detail.put("order_ct", row[3]);
			detail.put("material_un", row[4]);
			detail.put("detail_order_ed", row[5]); // ORDERDETAIL의 order_ed를 ORDER의 값으로 설정
			return detail;
		}).collect(Collectors.toList());

		result.put("details", details);

		return result;
	}

    @Transactional
    public void delete_OrderAndDetails(List<String> orderCds) {
    	orderRepository.delete_ORDERDETAIL(orderCds); // 디테일 먼저 삭제 처리
    	orderRepository.delete_ORDER(orderCds); // 그 후에 발주 데이터 삭제
    }
	
    
    
	// 발주 필터 데이터 조회
	public List<Map<String, Object>> select_FILTERED_ORDER(OrderFilterRequest filterRequest) {
		
		List<Object[]> result;
		
		result = orderRepository.select_FILTERED_ORDER(filterRequest);
		
		return result.stream().map(row -> {
			Map<String, Object> order = new HashMap<>();
			order.put("order_cd", row[0]);
			order.put("account_cd", row[1]);
			order.put("order_ps", row[2]);
			order.put("order_sd", row[3]);
			order.put("order_ed", row[4]);
			order.put("order_am", row[5]);
			order.put("order_mn", row[6]);
			order.put("order_st", row[7]);
			order.put("order_rm", row[8]);
			order.put("order_wr", row[9]);
			order.put("order_wd", row[10]);
			order.put("order_mf", row[11]);
			order.put("order_md", row[12]);
			
			return order;
			
		}).collect(Collectors.toList());
	}
    
    // 발주 상태 업데이트 실행
    @Transactional
    public void updateOrderStatus() {
        System.out.println("발주 상태 업데이트 실행...");
        orderRepository.updateOrderStatus();
        System.out.println("발주 상태 업데이트 완료!");
    }
	
	
	

}
