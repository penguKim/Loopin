package com.itwillbs.c4d2412t3p1.service;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.BomMaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.BomProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailProductInfoDTO;
import com.itwillbs.c4d2412t3p1.domain.DailyProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeListDTO;
import com.itwillbs.c4d2412t3p1.domain.LotResponse;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest.ProcessOrderDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseListDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkableEmployeeProjection;
import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.entity.Dailyproductplan;
import com.itwillbs.c4d2412t3p1.entity.DailyproductplanPK;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Lot;
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
import com.itwillbs.c4d2412t3p1.repository.DailyproductplanRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.LotRepository;
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

	private final DailyproductplanRepository dailyproductplanRepository;

	private final ProductplanprocessRepository productplanprocessRepository;

	private final ContractDetailRepository contractdetailRepository;

	private final EmployeeRepository employeeRepository;

	private final CommonRepository commonRepository;

	private final WarehouseRepository warehouseRepository;

	private final BomProcessRepository bomProcessRepository;

	private final BomRepository bomRepository;

	private final MaterialRepository materialRepository;

	private final LotRepository lotRepository;

	// 생산계획 등록 모달 진행중 상태 리스트 조회
	public List<ContractDetailDTO> select_CONTRACTCD_list(String contractCd) {
		List<ContractDetail> contractDetails = contractdetailRepository
				.select_CONTRACTDETAIL_BY_STATUS_list(contractCd);

		Map<String, Long> totalAmountMap = contractDetails.stream().collect(Collectors
				.groupingBy(ContractDetail::getContract_cd, Collectors.summingLong(ContractDetail::getProduct_am)));

		return contractDetails.stream()
				.map(detail -> new ContractDetailDTO(detail.getContract_cd(), detail.getProduct_cd(),
						detail.getProduct_sz(), detail.getProduct_cr(),
						totalAmountMap.getOrDefault(detail.getContract_cd(), (long) 0), // 총 수량 추가
						detail.getContract_ct(), detail.getContract_ed(), detail.getProduct_un()))
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
		
		log.info(result.toString());
		
		return result;
	}

//  최종 완제품 productCd를 만들기 위해 필요한
//  모든 원자재/부자재 + 소요량(합산) 목록 반환.
//  특정 제품(Product)의 모든 원자재/부자재 및 소요량 반환
	public List<BomMaterialDTO> findAllMaterialsForProduct(String productCd) {
	    // 자재별 총 소요량을 저장할 맵
	    Map<String, Long> materialAmountMap = new HashMap<>();
	    
	    // 탐색할 제품을 저장하는 스택 (제품 코드와 소요량 배수)
	    Deque<Pair<String, Long>> stack = new ArrayDeque<>();
	    stack.push(Pair.of(productCd, 1L));
	    
	    // 이미 탐색한 제품을 기록하여 중복 처리 방지
	    Set<String> visited = new HashSet<>();
	    
	    // 최종적으로 필요한 자재 코드들을 모아둘 집합 (배치 조회용)
	    Set<String> allMaterialCds = new HashSet<>();
	    
	    // Material의 자재 구분(materialGc)을 캐싱하는 Map
	    Map<String, String> materialGcCache = new HashMap<>();
	    
	    // 반복문을 통한 BOM 탐색
	    while (!stack.isEmpty()) {
	        Pair<String, Long> node = stack.pop();
	        String currentProduct = node.getKey();
	        Long multiplier = node.getValue();
	        
	        // 이미 방문한 제품은 건너뜀
	        if (!visited.add(currentProduct)) continue;
	        
	        // 현재 제품의 BOM 목록을 조회
	        List<Bom> bomList = bomRepository.findByProductCd(currentProduct);
	        for (Bom bom : bomList) {
	            String childCd = bom.getBom_cd();
	            Long total = bom.getBom_am() * multiplier;
	            
	            // 캐시에 materialGc가 있으면 재사용, 없으면 DB 호출 후 캐싱
	            String materialGc = materialGcCache.get(childCd);
	            if (materialGc == null) {
	                materialGc = materialRepository.findMaterialGcByMaterialCd(childCd);
	                materialGcCache.put(childCd, materialGc);
	            }
	            
	            // 자재 분류가 MATERIALS 또는 SUBMAT이면 최종 원자재로 처리
	            if ("MATERIALS".equals(materialGc) || "SUBMAT".equals(materialGc)) {
	                materialAmountMap.merge(childCd, total, Long::sum);
	                allMaterialCds.add(childCd);
	            } else {
	                // 반제품이라면 스택에 추가하여 재탐색
	                stack.push(Pair.of(childCd, total));
	            }
	        }
	    }
	    
	    // 최종적으로 모은 자재 코드를 한 번에 조회하여 N+1 문제 해소
	    List<Material> materials = materialRepository.findByMaterialCdIn(allMaterialCds);
	    Map<String, Material> materialMap = materials.stream()
	        .collect(Collectors.toMap(Material::getMaterial_cd, m -> m));
	    
	    // 결과 DTO로 변환
	    List<BomMaterialDTO> result = new ArrayList<>();
	    for (Map.Entry<String, Long> entry : materialAmountMap.entrySet()) {
	        Material material = materialMap.get(entry.getKey());
	        if (material != null) {
	            BomMaterialDTO dto = new BomMaterialDTO();
	            dto.setMaterial_cd(material.getMaterial_cd());
	            dto.setMaterial_nm(material.getMaterial_nm());
	            dto.setMaterial_gc(material.getMaterial_gc());
	            dto.setTotalAmount(entry.getValue());
	            result.add(dto);
	        }
	    }
	    
	    return result;
	}


	@Transactional
	public void save_PRODUCTPLAN(ProductPlanSaveRequest req) {
		// 1) productplan 저장
		ProductPlanSaveRequest.ProductPlanDTO planDTO = req.getProductplan();

		// (1) 다중 PK 생성
		ProductplanPK planPK = new ProductplanPK(planDTO.getContract_cd(), planDTO.getProduct_cd());

		// (2) 엔티티 생성
		Productplan planEntity = new Productplan();
		planEntity.setId(planPK);

		// (3) 문자열 날짜 → Timestamp 변환
		Timestamp startDate = null, endDate = null;
		if (planDTO.getProductplan_sd() != null && !planDTO.getProductplan_sd().isEmpty()) {
			startDate = Timestamp.valueOf(planDTO.getProductplan_sd() + " 00:00:00");
			planEntity.setProductplan_sd(startDate);
		}
		if (planDTO.getProductplan_ed() != null && !planDTO.getProductplan_ed().isEmpty()) {
			endDate = Timestamp.valueOf(planDTO.getProductplan_ed() + " 00:00:00");
			planEntity.setProductplan_ed(endDate);
		}

		planEntity.setProductplan_dd(planDTO.getProductplan_dd());
		planEntity.setWarehouse_cd(planDTO.getWarehouse_cd());
		planEntity.setProductplan_bg(planDTO.getProductplan_bg());

		Integer totalAmount = contractdetailRepository.findTotalContractAmount(planDTO.getContract_cd(),
				planDTO.getProduct_cd());
		planEntity.setProductplan_js(totalAmount != null ? totalAmount : 0); // null 방지

		// 4️) **초기 상태 = "대기" (일일생산계획이 등록되면 "계획"으로 변경됨)**
		planEntity.setProductplan_st("대기");

		// 5) DB 저장
		productplanRepository.save(planEntity);

		// 2) productplanprocess 저장 (기존 삭제 후 다시 저장)
		productplanprocessRepository.deleteByIdContractCdAndIdProductCd(planDTO.getContract_cd(),
				planDTO.getProduct_cd());

		for (ProductPlanSaveRequest.ProcessOrderDTO item : req.getProcessList()) {
			// (1) 다중 PK 생성
			ProductplanprocessPK processPK = new ProductplanprocessPK();
			processPK.setContract_cd(planDTO.getContract_cd());
			processPK.setProduct_cd(planDTO.getProduct_cd());
			processPK.setProcess_se(item.getProcess_se());

			// (2) 엔티티 생성
			Productplanprocess processEntity = new Productplanprocess();
			processEntity.setId(processPK);
			processEntity.setProcess_cd(item.getProcess_cd());

			// (3) DB 저장
			productplanprocessRepository.save(processEntity);
		}
	}

	public List<Map<String, Object>> select_PRODUCTPLAN_list() {
		return productplanRepository.findAll().stream().map(plan -> {
			Map<String, Object> resultMap = new HashMap<>();

			// 1️⃣ 수주번호 & 품목코드 (다중 PK에서 값 추출)
			resultMap.put("contract_cd", plan.getId().getContract_cd());
			resultMap.put("product_cd", plan.getId().getProduct_cd());

			// 2️⃣ 날짜 변환 제거 (뷰에서 처리할 것이므로 원본 `Timestamp` 그대로 전달)
			resultMap.put("productplan_sd", plan.getProductplan_sd());
			resultMap.put("productplan_ed", plan.getProductplan_ed());

			// 3️⃣ 기타 필드 매핑
			resultMap.put("productplan_dd", plan.getProductplan_dd());
			resultMap.put("productplan_js", plan.getProductplan_js());
			resultMap.put("warehouse_cd", plan.getWarehouse_cd());
			resultMap.put("productplan_st", plan.getProductplan_st()); // 등록 시 설정된 상태값 그대로 사용
			resultMap.put("productplan_bg", plan.getProductplan_bg());

			return resultMap;
		}).collect(Collectors.toList());
	}

//	일일 생산계획이 등록되면 "대기" → "계획" 상태로 업데이트
	@Transactional
	public void updatePlanStatusToPlanned(String contractCd, String productCd) {
		productplanRepository.findById(new ProductplanPK(contractCd, productCd)).ifPresent(plan -> {
			plan.setProductplan_st("계획");
			productplanRepository.save(plan);
		});
	}

//	작업이 실제 시작되면 "계획" → "진행중" 상태로 변경
	@Transactional
	public void updatePlanStatusToInProgress(String contractCd, String productCd) {
		productplanRepository.findById(new ProductplanPK(contractCd, productCd)).ifPresent(plan -> {
			plan.setProductplan_st("진행중");
			productplanRepository.save(plan);
		});
	}

//	작업 완료 시 "진행중" → "완료" 상태로 변경
	@Transactional
	public void updatePlanStatusToCompleted(String contractCd, String productCd) {
		productplanRepository.findById(new ProductplanPK(contractCd, productCd)).ifPresent(plan -> {
			plan.setProductplan_st("완료");
			productplanRepository.save(plan);
		});
	}

	@Transactional
	public void deleteProductPlanList(List<Map<String, String>> requestList) {
		for (Map<String, String> item : requestList) {
			String contractCd = item.get("contract_cd");
			String productCd = item.get("product_cd");

			// 1) 생산계획 엔티티 조회
			ProductplanPK pk = new ProductplanPK(contractCd, productCd);
			Optional<Productplan> planOpt = productplanRepository.findById(pk);
			if (planOpt.isPresent()) {
				Productplan plan = planOpt.get();

				// 2) 상태 확인
				if ("대기".equals(plan.getProductplan_st())) {
					// 3) 먼저 productplanprocess(공정 순서) 삭제
					productplanprocessRepository.deleteByIdContractCdAndIdProductCd(contractCd, productCd);
					// 4) 생산계획 삭제
					productplanRepository.delete(plan);
				} else {
					// "대기" 상태가 아니면 무시 or 예외
					// throw new IllegalStateException("대기 상태가 아닌 계획은 삭제할 수 없습니다.");
				}
			}
		}
	}

	public List<ProcessOrderDTO> selectPRODUCTPLANPROCESSList(String contract_cd, String product_cd) {
		List<Productplanprocess> entityList = productplanprocessRepository.findByContractCdAndProductCd(contract_cd,
				product_cd);

		// ProcessOrderDTO : { process_cd, process_se }
		return entityList.stream().map(proc -> {
			ProcessOrderDTO dto = new ProcessOrderDTO();
			dto.setProcess_cd(proc.getProcess_cd());
			dto.setProcess_se(proc.getId().getProcess_se()); // 복합PK에서 process_se
			return dto;
		}).collect(Collectors.toList());
	}

	public List<ProductPlanProcessDTO> getProcessList(String contractCd, String productCd) {
		// 하위 공정 (순서 포함)
		List<ProductPlanProcessDTO> processList = productplanprocessRepository
				.findProcessesByContractAndProduct(contractCd, productCd);

		// 상위 공정 (순서 없음, 중복 제거됨)
		List<ProductPlanProcessDTO> bomProcessList = bomProcessRepository.findBomProcessesByProduct(productCd);

		// 중복된 하위 공정을 제거하고 병합
		Set<String> existingProcessCds = processList.stream().map(ProductPlanProcessDTO::getProcess_cd)
				.collect(Collectors.toSet());

		// 상위 공정 중 하위 공정에 포함되지 않은 것만 추가
		for (ProductPlanProcessDTO bom : bomProcessList) {
			if (!existingProcessCds.contains(bom.getProcess_cd())) {
				processList.add(bom);
			}
		}

		return processList;
	}

	public List<ContractDetailProductInfoDTO> findColorSizeList(String contractCd, String productCd) {
		// 1) 수주상세 목록 조회
		List<ContractDetail> detailList = contractdetailRepository.findByContractCdAndProductCd(contractCd, productCd);

		// 2) DTO 변환
		return detailList.stream()
				.map(d -> new ContractDetailProductInfoDTO(d.getProduct_cr(), d.getProduct_sz(), d.getProduct_am()))
				.collect(Collectors.toList());
	}

	public List<WorkableEmployeeProjection> select_WORKABLE_EMPLOYEE_list(String workDate, String productCd,
			String processCd) {
		return productplanRepository.findWorkableEmployees(workDate, productCd, processCd);
	}

	@Transactional
	public void saveDailyPlan(DailyProductPlanDTO dto) {
		// 1) 복합키 생성
		DailyproductplanPK pk = new DailyproductplanPK();

		// 날짜 변환
		if (dto.getDailyproductplan_sd() != null && !dto.getDailyproductplan_sd().isEmpty()) {
			pk.setDailyproductplan_sd(Timestamp.valueOf(dto.getDailyproductplan_sd() + " 00:00:00"));
		}

		pk.setContract_cd(dto.getContract_cd());

		// 🔹 (중요) product_cd 컬럼에 “base_product_cd + '-' + size + '-' + color” 형식으로 합성
		String combinedCd = dto.getBase_product_cd() + "-" + dto.getProduct_sz() + "-" + dto.getProduct_cr();
		pk.setProduct_cd(combinedCd);

		pk.setProcess_cd(dto.getProcess_cd());
		pk.setProduct_cr(dto.getProduct_cr());
		pk.setProduct_sz(dto.getProduct_sz());

		// 2) 엔티티 생성
		Dailyproductplan entity = new Dailyproductplan();
		entity.setId(pk);
		entity.setDailyproductplan_js(dto.getDailyproductplan_js());
		entity.setProcess_se(dto.getProcess_se());

		// 3) DB 저장
		dailyproductplanRepository.save(entity);
	}

	/**
	 * 특정 (contract_cd, product_cd)의 일일생산계획 목록 조회
	 */
	public List<DailyProductPlanDTO> findDailyPlanList(String contractCd, String baseProductCd) {
		// 1) 엔티티 목록 조회
		List<Dailyproductplan> entityList = dailyproductplanRepository.findAllByContractAndBaseProduct(contractCd,
				baseProductCd);

		// 2) Entity → DTO 변환
		return entityList.stream().map(e -> {
			DailyProductPlanDTO dto = new DailyProductPlanDTO();
			// 날짜
			if (e.getId().getDailyproductplan_sd() != null) {
				dto.setDailyproductplan_sd(
						e.getId().getDailyproductplan_sd().toLocalDateTime().toLocalDate().toString());
			}
			dto.setContract_cd(e.getId().getContract_cd());

			// DB에는 "JORDAN001-220-BLACK" 형태가 들어 있음
			// 하지만 DTO에는 base_product_cd, product_cr, product_sz로 분리
			String fullCd = e.getId().getProduct_cd(); // ex) "JORDAN001-220-BLACK"
			// 첫 '-' 직전까지 = base_product_cd
			int idx = fullCd.indexOf('-');
			if (idx > 0) {
				dto.setBase_product_cd(fullCd.substring(0, idx));
			}
			// 이미 e.getId().getProduct_cr() / e.getId().getProduct_sz()에 값이 있으므로,
			// dto.setProduct_cr(e.getId().getProduct_cr());
			// dto.setProduct_sz(e.getId().getProduct_sz());
			// 혹은 fullCd를 further split해서 사이즈/색상 추출 가능 (업무 로직에 따라)

			dto.setProcess_cd(e.getId().getProcess_cd());
			dto.setProduct_cr(e.getId().getProduct_cr());
			dto.setProduct_sz(e.getId().getProduct_sz());
			dto.setDailyproductplan_js(e.getDailyproductplan_js());
			dto.setProcess_se(e.getProcess_se());
			return dto;
		}).collect(Collectors.toList());
	}

	// 작업지시

	/**
	 * LOT 테이블 전체 목록 조회 -> LotResponse DTO 변환
	 */
	public List<LotResponse> getLotList() {
		// 1) 엔티티 목록 조회
		List<Lot> entityList = lotRepository.findAll();

		// 2) 엔티티 -> DTO 변환
		return entityList.stream().map(lot -> {
			LotResponse dto = new LotResponse();

			// (1) 복합키 (lot_cd, process_cd)
			dto.setLot_cd(lot.getId().getLot_cd());
			dto.setProcess_cd(lot.getId().getProcess_cd());

			// (2) contract_cd
			dto.setContract_cd(lot.getContract_cd());

			// (3) lot_cr -> 문자열 변환 (예: "yyyy-MM-dd HH:mm:ss")
			if (lot.getLot_cr() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dto.setLot_cr(sdf.format(lot.getLot_cr()));
			} else {
				dto.setLot_cr("");
			}

			// (4) product_cd
			dto.setProduct_cd(lot.getProduct_cd());

			// (5) 색상, 사이즈
			dto.setProduct_cr(lot.getProduct_cr());
			dto.setProduct_sz(lot.getProduct_sz());

			// (6) 수량 (dailyproductplan_js)
			dto.setDailyproductplan_js(lot.getDailyproductplan_js());

			return dto;
		}).collect(Collectors.toList());
	}

	public String findDailyPlanDate(String baseProductCd, String contractCd, String processCd, String productCr,
			String productSz) {
		return dailyproductplanRepository.findDailyPlanDate(baseProductCd, contractCd, processCd, productCr, productSz);
	}

}
