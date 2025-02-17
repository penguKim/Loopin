package com.itwillbs.c4d2412t3p1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeListDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseListDTO;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Productplan;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.ContractDetailRepository;
import com.itwillbs.c4d2412t3p1.repository.ContractRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.ProductplanRepository;
import com.itwillbs.c4d2412t3p1.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ProductplanService {

	private final ProductplanRepository productplanRepository;

	private final ContractDetailRepository contractdetailRepository;

	private final EmployeeRepository employeeRepository;
	
	private final CommonRepository commonRepository;
	
	private final WarehouseRepository warehouseRepository;
	
	// 생산계획 등록 모달 진행중 상태 리스트 조회
	public List<ContractDetailDTO> select_CONTRACTCD_list(String contractCd) {
		List<ContractDetail> contractDetails = contractdetailRepository
				.select_CONTRACTDETAIL_BY_STATUS_list(contractCd);

		return contractDetails.stream()
				.map(detail -> new ContractDetailDTO(detail.getContract_cd(), detail.getProduct_cd(),
						detail.getProduct_sz(), detail.getProduct_cr(), detail.getProduct_am(), detail.getContract_ct(),
						detail.getContract_ed(), detail.getProduct_un()))
				.collect(Collectors.toList());
	}
	// 생산계획 등록 모달 생산부서 담당자 리스트 조회
	public List<EmployeeListDTO> select_EMPLOYEE_BY_DP(String employee_cd) {
        List<Employee> employees;
        if (employee_cd != null && !employee_cd.trim().isEmpty()) {
            // 사원번호 조건과 부서가 '60'인 조건 모두 적용
            employees = employeeRepository.select_EMPLOYEE_BY_CD_DP(employee_cd, "60");
        } else {
            // 사원번호 미전달 시 부서 '60'인 전체 사원 조회
            employees = employeeRepository.select_EMPLOYEE_BY_DP("60");
        }
        
        // 부서와 직급 공통코드 조회 (공통코드 DTO 활용)
        List<Common_codeDTO> deptList = commonRepository.select_COMMON_list("DEPARTMENT");
        List<Common_codeDTO> posList = commonRepository.select_COMMON_list("POSITION");
        
        // 코드값 -> 코드명 매핑 생성
        Map<String, String> deptMap = deptList.stream()
                .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
        Map<String, String> posMap = posList.stream()
                .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
        
        // Employee 엔티티 정보를 EmployeeListDTO로 변환
        List<EmployeeListDTO> dtoList = new ArrayList<>();
        for (Employee emp : employees) {
            EmployeeListDTO dto = new EmployeeListDTO();
            dto.setEmployee_cd(emp.getEmployee_cd());
            dto.setEmployee_nm(emp.getEmployee_nm());
            dto.setDepartmentName(deptMap.get(emp.getEmployee_dp()));
            dto.setPositionName(posMap.get(emp.getEmployee_gd()));
            dtoList.add(dto);
        }
        return dtoList;
    }
	
	public List<WarehouseListDTO> select_WAREHOUSE_BY_TP(String warehouse_cd) {
        List<Warehouse> warehouses;
        if (warehouse_cd != null && !warehouse_cd.trim().isEmpty()) {
            warehouses = warehouseRepository.select_WAREHOUSE_BY_CD_TP(warehouse_cd);
        } else {
            warehouses = warehouseRepository.select_WAREHOUSE_BY_TP();
        }
        
        // 창고유형(Common code) 조회: common_gc가 'WHTYPE'인 경우
        List<Common_codeDTO> whTypeList = commonRepository.select_COMMON_list("WHTYPE");
        Map<String, String> whTypeMap = whTypeList.stream()
                .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
        
        List<WarehouseListDTO> dtoList = new ArrayList<>();
        for (Warehouse wh : warehouses) {
            WarehouseListDTO dto = new WarehouseListDTO();
            dto.setWarehouse_cd(wh.getWarehouse_cd());
            dto.setWarehouse_nm(wh.getWarehouse_nm());
            // warehouse_tp(예: 'PROCESS')에 해당하는 창고유형명을 매핑
            dto.setWarehouseName(whTypeMap.get(wh.getWarehouse_tp()));
            dto.setWarehouse_rm(wh.getWarehouse_rm());
            dtoList.add(dto);
        }
        return dtoList;
    }
	
}
