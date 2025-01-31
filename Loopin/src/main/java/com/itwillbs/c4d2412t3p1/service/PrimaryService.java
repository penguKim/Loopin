package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.mapper.PrimaryMapper;
import com.itwillbs.c4d2412t3p1.repository.WarehouseRepository;
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



}
