package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Product;
import com.itwillbs.c4d2412t3p1.entity.ProductPK;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.mapper.PrimaryMapper;
import com.itwillbs.c4d2412t3p1.repository.ProductRepository;
import com.itwillbs.c4d2412t3p1.repository.WareareaRepository;
import com.itwillbs.c4d2412t3p1.repository.WarehouseRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class PrimaryService {
	
	private final PrimaryMapper primaryMapper;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;
	private final WareareaRepository wareareaRepository;

	// 창고 관리 ----------------------------------------------------------------
	public List<WarehouseDTO> select_WAREHOUSE_list(WarehouseFilterRequest filter) {
		return primaryMapper.select_WAREHOUSE_list(filter);
	}

	// 창고 등록
	@Transactional
	public void insert_WAREHOUSE(WarehouseDTO warehouseDTO, List<WareareaDTO> wareareaDTOList) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
        Warehouse warehouse = warehouseRepository.findById(warehouseDTO.getWarehouse_cd())
            .map(existing -> {
                existing.setWarehouse_nm(warehouseDTO.getWarehouse_nm());
                existing.setWarehouse_tp(warehouseDTO.getWarehouse_tp());
                existing.setWarehouse_in(warehouseDTO.getWarehouse_in());
                existing.setWarehouse_us(warehouseDTO.isWarehouse_us());
                existing.setWarehouse_rm(warehouseDTO.getWarehouse_rm());
                existing.setWarehouse_uu(regUser);
                existing.setWarehouse_ud(time);
                return existing;
            })
            .orElseGet(() -> {
                warehouseDTO.setWarehouse_ru(regUser);
                warehouseDTO.setWarehouse_rd(time);
                return Warehouse.setWarehouse(warehouseDTO);
            });
        
        // 창고 등록
        warehouseRepository.save(warehouse);
        // 기존 구역 삭제
        wareareaRepository.deleteByWarehouseCd(warehouseDTO.getWarehouse_cd());

        List<Warearea> wareareaList = wareareaDTOList.stream()
            .map(warearea -> {
            	warearea.setWarehouse_cd(warehouseDTO.getWarehouse_cd());
                return Warearea.setWarehouse(warearea);
            })
            .collect(Collectors.toList());
        // 구역 등록
        wareareaRepository.saveAll(wareareaList);
	}
	
	// 창고코드 중복 체크
	public boolean check_WAREHOUSE_CD(String warehouse_cd) {
		return warehouseRepository.findById(warehouse_cd).isEmpty();
	}

	// 창고 상세 조회
	public Warehouse select_WAREHOUSE_detail(String warehouse_cd) {
		return warehouseRepository.findById(warehouse_cd).orElse(null);
	}
	

	// 창고구역 조회
	public List<Warearea> select_WAREAREA_list(String warehouse_cd) {
		return wareareaRepository.findByWarehouseCd(warehouse_cd);
	}

	// 창고 삭제
	@Transactional
	public void delete_WAREHOUSE(List<WarehouseDTO> warehouseList) {
		// 창고에 재고가 있을 경우 삭제안되게 로직 수정
		
	    List<String> warehouseCodes = warehouseList.stream()
	        .map(WarehouseDTO::getWarehouse_cd)
	        .collect(Collectors.toList());
	    
	    wareareaRepository.deleteByWarehouseCdIn(warehouseCodes);
	    
	    warehouseRepository.deleteAllById(warehouseCodes);
	}

	


	
	// 제품 관리 ----------------------------------------------------------------4
	
	// 제품 조회
	public List<ProductDTO> select_PRODUCT_list(ProductFilterRequest filter) {
		return primaryMapper.select_PRODUCT_list(filter);
	}

	// 창고 목록 조회
	public List<Map<String, String>> select_WAREHOUSE_code() {
		return primaryMapper.select_WAREHOUSE_code();
	}

	// 제품 등록
	public Product insert_PRODUCT(ProductDTO productDTO, List<String> sizeList, List<String> colorList) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
//        Product product = productRepository.findById(new ProductPK(productDTO.getProduct_cd(), productDTO.getItem_cd()))
		Product product = productRepository.findById(new ProductPK(productDTO.getProduct_cd(), "MAT"))
            .map(existing -> {
            	// DTO -> 엔티티 변환
//                existing.setWarehouse_nm(warehouseDTO.getWarehouse_nm());
//                existing.setWarehouse_tp(warehouseDTO.getWarehouse_tp());
//                existing.setWarehouse_in(warehouseDTO.getWarehouse_in());
//                existing.setWarehouse_us(warehouseDTO.isWarehouse_us());
//                existing.setWarehouse_rm(warehouseDTO.getWarehouse_rm());
                existing.setProduct_uu(regUser);
                existing.setProduct_ud(time);
                return existing;
            })
            .orElseGet(() -> {
            	productDTO.setItem_cd("TEST");
            	productDTO.setProduct_sz(String.join(",", sizeList));
            	productDTO.setProduct_cr(String.join(",", colorList));
            	productDTO.setProduct_ru(regUser);
            	productDTO.setProduct_rd(time);
                return Product.setProduct(productDTO);
            });
        
		
		
		
        return productRepository.save(product);
	}



}
