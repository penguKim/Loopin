package com.itwillbs.c4d2412t3p1.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.BomMaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.BomProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeListDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest;
import com.itwillbs.c4d2412t3p1.domain.WarehouseListDTO;
import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Material;
import com.itwillbs.c4d2412t3p1.entity.Productplan;
import com.itwillbs.c4d2412t3p1.entity.ProductplanPK;
import com.itwillbs.c4d2412t3p1.entity.Productplanprocess;
import com.itwillbs.c4d2412t3p1.entity.ProductplanprocessPK;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.repository.BomProcessRepository;
import com.itwillbs.c4d2412t3p1.repository.BomRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.ContractDetailRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.MaterialRepository;
import com.itwillbs.c4d2412t3p1.repository.ProductplanRepository;
import com.itwillbs.c4d2412t3p1.repository.ProductplanprocessRepository;
import com.itwillbs.c4d2412t3p1.repository.WarehouseRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ProductplanService {

	private final ProductplanRepository productplanRepository;

	private final ProductplanprocessRepository productplanprocessRepository;

	private final ContractDetailRepository contractdetailRepository;

	private final EmployeeRepository employeeRepository;

	private final CommonRepository commonRepository;

	private final WarehouseRepository warehouseRepository;

	private final BomProcessRepository bomProcessRepository;

	private final BomRepository bomRepository;

	private final MaterialRepository materialRepository;

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

	public List<BomProcessDTO> select_BOMPROCESS_BY_CD(String product_cd) {
		// 1) DB에서 raw data 목록 조회 (List<Object[]>)
		List<Object[]> rows = bomProcessRepository.selectRawData(product_cd);

		// 2) List<Object[]> → List<BomProcessDTO>로 변환
		List<BomProcessDTO> result = new ArrayList<>();
		for (Object[] row : rows) {
			// row[0] : product_cd (String)
			// row[1] : process_cd (String)
			// row[2] : bomprocess_cd (String)
			// row[3] : bomprocess_ap (String)
			// row[4] : bomprocess_rt (String)
			BomProcessDTO dto = new BomProcessDTO();
			dto.setProduct_cd((String) row[0]);
			dto.setProcess_cd((String) row[1]);
			dto.setBomprocess_cd((String) row[2]);
			dto.setBomprocess_ap((String) row[3]);
			dto.setBomprocess_rt((String) row[4]);
			// 필요한 필드만 매핑
			result.add(dto);
		}

		return result;
	}


	
//     최종 완제품 productCd를 만들기 위해 필요한
//     모든 원자재/부자재 + 소요량(합산) 목록 반환.
//     특정 제품(Product)의 모든 원자재/부자재 및 소요량 반환
    public List<BomMaterialDTO> findAllMaterialsForProduct(String productCd) {
        // 자재별 총 소요량
        Map<String, Long> materialAmountMap = new HashMap<>();

        // 경로 기반 중복 탐색 방지용 Set
        Set<String> visited = new HashSet<>();

        // 재귀 호출
        collectBOM(productCd, 1L, materialAmountMap, new HashSet<>());

        // 결과 DTO 변환
        List<BomMaterialDTO> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : materialAmountMap.entrySet()) {
            String materialCd = entry.getKey();
            Long totalAmount = entry.getValue();

            Material material = materialRepository.findMaterialEntityByCd(materialCd);
            if (material != null) {
                BomMaterialDTO dto = new BomMaterialDTO();
                dto.setMaterial_cd(material.getMaterial_cd());
                dto.setMaterial_nm(material.getMaterial_nm());
                dto.setMaterial_gc(material.getMaterial_gc());
                dto.setTotalAmount(totalAmount);
                result.add(dto);
            }
        }

        return result;
    }

    /**
     * 재귀 메서드: BOM 재귀 탐색 (경로 기반 중복 방지 포함)
     *
     * @param currentProduct 현재 탐색 중인 제품 코드
     * @param multiplier     상위 BOM의 소요량 배수
     * @param accumMap       자재별 누적 소요량 맵
     * @param pathSet        경로 기반 중복 방지용 Set
     */
    private void collectBOM(String currentProduct, Long multiplier,
                            Map<String, Long> accumMap,
                            Set<String> pathSet) {

        // 경로 중복 방지 (경로 내 동일 제품 재탐색 금지)
        if (pathSet.contains(currentProduct)) {
            return;
        }
        pathSet.add(currentProduct);

        // 현재 제품의 BOM 조회
        List<Bom> bomList = bomRepository.findByProductCd(currentProduct);

        for (Bom bom : bomList) {
            String childCd = bom.getBom_cd();
            Long childAmount = bom.getBom_am();
            Long total = childAmount * multiplier;

            // MATERIAL 구분 조회
            String materialGc = materialRepository.findMaterialGcByMaterialCd(childCd);

            if ("MATERIALS".equals(materialGc) || "SUBMAT".equals(materialGc)) {
                // 자재면 누적
                accumMap.merge(childCd, total, Long::sum);
            } else {
                // 반제품이면 재귀 탐색
                collectBOM(childCd, total, accumMap, new HashSet<>(pathSet));
            }
        }

        // 경로 해제 (재귀 종료)
        pathSet.remove(currentProduct);
    }








	@Transactional
	public void save_PRODUCTPLAN(ProductPlanSaveRequest req) {
		// 1) productplan 저장
		ProductPlanSaveRequest.ProductPlanDTO planDTO = req.getProductplan();

		// (1) 임베디드 PK 생성
		ProductplanPK planPK = new ProductplanPK(planDTO.getContract_cd(), planDTO.getProduct_cd());

		// (2) 엔티티 생성
		Productplan planEntity = new Productplan();
		planEntity.setId(planPK);

		// (3) 문자열 날짜 -> Timestamp 변환 (예시)
		// 필요하다면 LocalDateTime을 쓰는 방법도 있음
		if (planDTO.getProductplan_sd() != null && !planDTO.getProductplan_sd().isEmpty()) {
			// "2025-03-18" → "2025-03-18 00:00:00"
			String sdStr = planDTO.getProductplan_sd() + " 00:00:00";
			planEntity.setProductplan_sd(Timestamp.valueOf(sdStr));
		}
		if (planDTO.getProductplan_ed() != null && !planDTO.getProductplan_ed().isEmpty()) {
			String edStr = planDTO.getProductplan_ed() + " 00:00:00";
			planEntity.setProductplan_ed(Timestamp.valueOf(edStr));
		}

		planEntity.setProductplan_dd(planDTO.getProductplan_dd());
		planEntity.setProductplan_js(planDTO.getProductplan_js());
		planEntity.setWarehouse_cd(planDTO.getWarehouse_cd());
		planEntity.setProductplan_st(planDTO.getProductplan_st());
		planEntity.setProductplan_bg(planDTO.getProductplan_bg());

		// (4) DB 저장 (기존 레코드가 있으면 덮어쓸 수도 있고, merge 필요 시 확인)
		productplanRepository.save(planEntity);

		// 2) productplanprocess 저장
		// (contract_cd, product_cd) 동일 -> process_se + process_cd
		// 기존 레코드가 있으면 삭제/갱신 여부는 정책에 따라 결정
		productplanprocessRepository.deleteByIdContractCdAndIdProductCd(planDTO.getContract_cd(),
				planDTO.getProduct_cd());

		for (ProductPlanSaveRequest.ProcessOrderDTO item : req.getProcessList()) {
			// 임베디드 PK
			ProductplanprocessPK processPK = new ProductplanprocessPK();
			processPK.setContract_cd(planDTO.getContract_cd());
			processPK.setProduct_cd(planDTO.getProduct_cd());
			processPK.setProcess_se(item.getProcess_se()); // 순서 (String으로 저장 or int 변환)

			// 엔티티
			Productplanprocess processEntity = new Productplanprocess();
			processEntity.setId(processPK);
			processEntity.setProcess_cd(item.getProcess_cd()); // 실제 공정코드

			productplanprocessRepository.save(processEntity);
		}
	}

}
