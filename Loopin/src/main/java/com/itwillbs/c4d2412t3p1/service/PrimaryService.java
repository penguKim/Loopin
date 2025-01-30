package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Product;
import com.itwillbs.c4d2412t3p1.entity.ProductPK;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.mapper.PrimaryMapper;
import com.itwillbs.c4d2412t3p1.repository.ProductRepository;
import com.itwillbs.c4d2412t3p1.repository.WarehouseRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class PrimaryService {
	
	private final PrimaryMapper primaryMapper;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;

	// 창고 관리 ----------------------------------------------------------------
	public List<WarehouseDTO> select_WAREHOUSE_list(WarehouseFilterRequest filter) {
		return primaryMapper.select_WAREHOUSE_list(filter);
	}

	// 창고 등록
	public Warehouse insert_WAREHOUSE(WarehouseDTO warehouseDTO) {
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
        
        return warehouseRepository.save(warehouse);
	}

	// 창고 상세 조회
	public Warehouse select_WAREHOUSE_detail(String warehouse_cd) {
		return warehouseRepository.findById(warehouse_cd).orElse(null);
	}

	// 창고 삭제
	@Transactional
	public void delete_WAREHOUSE(List<WarehouseDTO> warehouseList) {
	    List<String> warehouseCodes = warehouseList.stream()
	        .map(WarehouseDTO::getWarehouse_cd)
	        .collect(Collectors.toList());
	        
	    warehouseRepository.deleteAllById(warehouseCodes);
	}

	
	public boolean check_WAREHOUSE_CD(String warehouse_cd) {
		return warehouseRepository.findById(warehouse_cd).isEmpty();
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
