package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.itwillbs.c4d2412t3p1.domain.ContractDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.entity.Contract;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.repository.ContractRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ContractFilterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class BusinessService {

	private final ContractRepository contractRepository;

	public List<Map<String, Object>> select_RPODUCT() {

		List<Object[]> result;

		result = contractRepository.select_RPODUCT();

		return result.stream().map(row -> {
			Map<String, Object> product = new HashMap<>();
			product.put("product_cd", row[0]);
			product.put("product_nm", row[1]);
			product.put("product_cr", row[2]);
			product.put("product_sz", row[3]);
			product.put("product_un", row[4]);

			return product;

		}).collect(Collectors.toList());
	}

	
	public List<Map<String, Object>> search_PRODUCT(String keyword) {

	    List<Object[]> result;

	    result = contractRepository.search_PRODUCT(keyword);

	    return result.stream().map(row -> {
	        Map<String, Object> product = new HashMap<>();
	        product.put("product_cd", row[0]);
	        product.put("product_nm", row[1]);
	        product.put("product_cr", row[2]);
	        product.put("product_sz", row[3]);
	        product.put("product_un", row[4]);

	        return product;

	    }).collect(Collectors.toList());
	}

	
	
	public List<Map<String, Object>> select_CONTRACT() {

		List<Object[]> result;

		result = contractRepository.select_CONTRACT();

		return result.stream().map(row -> {
			Map<String, Object> contract = new HashMap<>();
			contract.put("contract_cd", row[0]);
			contract.put("account_cd", row[1]);
			contract.put("employee_cd", row[2]);
			contract.put("contract_ps", row[3]);
			contract.put("contract_sd", row[4]);
			contract.put("contract_ed", row[5]);
			contract.put("contract_am", row[6]);
			contract.put("contract_mn", row[7]);
			contract.put("contract_st", row[8]);
			contract.put("contract_rm", row[9]);
			contract.put("contract_wr", row[10]);
			contract.put("contract_wd", row[11]);
			contract.put("contract_mf", row[12]);
			contract.put("contract_md", row[13]);

			return contract;

		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> select_ACCOUNT_CONTRACT() {

		List<Object[]> result;

		result = contractRepository.select_ACCOUNT_CONTRACT();

		return result.stream().map(row -> {
			Map<String, Object> account = new HashMap<>();
			account.put("account_cd", row[0]);
			account.put("account_nm", row[1]);

			return account;

		}).collect(Collectors.toList());
	}
	
	public List<Map<String, Object>> search_ACCOUNT_CONTRACT(String keyword) {
		
		List<Object[]> result;
		
		result = contractRepository.search_ACCOUNT_CONTRACT(keyword);
		
		return result.stream().map(row -> {
			Map<String, Object> account = new HashMap<>();
			account.put("account_cd", row[0]);
			account.put("account_nm", row[1]);
			
			return account;
			
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> select_CONTRACT_PS() {

		List<Object[]> result;

		result = contractRepository.select_CONTRACT_PS();

		return result.stream().map(row -> {
			Map<String, Object> contract_ps = new HashMap<>();
			contract_ps.put("employee_cd", row[0]);
			contract_ps.put("employee_nm", row[1]);
			contract_ps.put("employee_dp", row[2]);
			contract_ps.put("employee_gd", row[3]);

			return contract_ps;

		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> search_CONTRACT_PS(String keyword) {
		
		List<Object[]> result;
		
		result = contractRepository.search_CONTRACT_PS(keyword);
		
		return result.stream().map(row -> {
			Map<String, Object> contract_ps = new HashMap<>();
			contract_ps.put("employee_cd", row[0]);
			contract_ps.put("employee_nm", row[1]);
			contract_ps.put("employee_dp", row[2]);
			contract_ps.put("employee_gd", row[3]);
			
			return contract_ps;
			
		}).collect(Collectors.toList());
	}

	@Transactional
	public void insert_CONTRACT(ContractDTO contractDto, List<ContractDetailDTO> details) throws IOException {

		// 시퀀스를 통해 contract_cd 생성 및 헤드 저장
		Long sequenceValue = contractRepository.getNextSequenceValue();
		Contract contract = Contract.createContract(contractDto, sequenceValue);
		contractRepository.save(contract);

		for (ContractDetailDTO detail : details) {
			ContractDetail contractDetail = ContractDetail.builder()
					.contract_cd(contract.getContract_cd())
					.product_cd(detail.getProduct_cd())
					.product_sz(detail.getProduct_sz())
					.product_cr(detail.getProduct_cr())
					.product_am(detail.getProduct_am())
					.contract_ct(detail.getContract_ct())
					.contract_ed(detail.getContract_ed())
					.product_un(detail.getProduct_un())
					.build();

			contractRepository.saveContractDetail(contractDetail);
		}
	}
	
	
	public void update_CONTRACT(ContractDTO contractDto, List<ContractDetailDTO> details) throws IOException {
	    // 기존 수주 정보 조회
	    Contract existingContract = contractRepository.findById(contractDto.getContract_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 계약을 찾을 수 없습니다: " + contractDto.getContract_cd()));

	    //  작성자와 작성일 유지
	    contractDto.setContract_wr(existingContract.getContract_wr()); // 작성자 유지
	    contractDto.setContract_wd(existingContract.getContract_wd()); // 작성일 유지

	    // 기존 수주 정보 업데이트
	    existingContract.setAccount_cd(contractDto.getAccount_cd());
	    existingContract.setEmployee_cd(contractDto.getEmployee_cd());
	    existingContract.setContract_ps(contractDto.getContract_ps());
	    existingContract.setContract_sd(contractDto.getContract_sd());
	    existingContract.setContract_ed(contractDto.getContract_ed());
	    existingContract.setContract_am(contractDto.getContract_am());
	    existingContract.setContract_mn(contractDto.getContract_mn());
	    existingContract.setContract_st(contractDto.getContract_st());
	    existingContract.setContract_rm(contractDto.getContract_rm());
	    existingContract.setContract_wr(contractDto.getContract_wr());
	    existingContract.setContract_wd(contractDto.getContract_wd());
	    existingContract.setContract_mf(contractDto.getContract_mf());
	    existingContract.setContract_md(contractDto.getContract_md());

	    
	    // 변경된 계약 저장
	    contractRepository.save(existingContract);
	    
	    // 기존 상세 정보 삭제
	    contractRepository.deleteContractDetailsByContractCd(contractDto.getContract_cd());
	   
	    // 새로운 상세 정보 저장
	    for (ContractDetailDTO detail : details) {
	        ContractDetail contractDetail = ContractDetail.builder()
	            .contract_cd(contractDto.getContract_cd())
	            .product_cd(detail.getProduct_cd())
	            .product_sz(detail.getProduct_sz())
	            .product_cr(detail.getProduct_cr())
	            .product_am(detail.getProduct_am())
	            .contract_ct(detail.getContract_ct())
	            .contract_ed(contractDto.getContract_ed()) // CONTRACTDETAIL의 contract_ed를 CONTRACT의 값으로 설정
	            .product_un(detail.getProduct_un())
	            .build();

	        contractRepository.saveContractDetail(contractDetail);
	    }

	}
	

	public List<Map<String, Object>> select_CONTRACTDETAIL(String contract_cd) {
		// Repository에서 데이터 조회
		List<Object[]> result = contractRepository.select_CONTRACTDETAIL(contract_cd);

		// Object[] 데이터를 Map<String, Object>로 변환
		return result.stream().map(row -> {
			Map<String, Object> detail = new HashMap<>();
			detail.put("contract_cd", row[0]);
			detail.put("product_cd", row[1]);
			detail.put("product_sz", row[2]);
			detail.put("product_cr", row[3]);
			detail.put("product_am", row[4]);
			detail.put("contract_ct", row[5]);
			detail.put("product_un", row[6]);
			detail.put("contract_ed", row[7]);

			return detail;
		}).collect(Collectors.toList());
	}

	public Map<String, Object> getContractDetails(String contractCd) {
		Map<String, Object> result = new HashMap<>();

		// 수주 정보 조회
		List<Object[]> contractList = contractRepository.findContractByCd(contractCd);
		
		if (!contractList.isEmpty()) {
			Object[] row = contractList.get(0); // 단일 계약이므로 첫 번째 행 사용
			Map<String, Object> contract = new HashMap<>();
			contract.put("contract_cd", row[0]);
			contract.put("account_cd", row[1]);
			contract.put("employee_cd", row[2]);
			contract.put("contract_ps", row[3]);
			contract.put("contract_sd", row[4]);
			contract.put("contract_ed", row[5]);
			contract.put("contract_am", row[6]);
			contract.put("contract_mn", row[7]);
			contract.put("contract_st", row[8]);
			contract.put("contract_rm", row[9]);
			contract.put("contract_wr", row[10]);
			contract.put("contract_wd", row[11]);
			contract.put("contract_mf", row[12]);
			contract.put("contract_md", row[13]);

			result.put("contract", contract);
		}

		// 상세 정보 조회
		List<Object[]> detailList = contractRepository.select_CONTRACTDETAIL(contractCd);
		List<Map<String, Object>> details = detailList.stream().map(row -> {
			Map<String, Object> detail = new HashMap<>();
			detail.put("contract_cd", row[0]);
			detail.put("product_cd", row[1]);
			detail.put("product_sz", row[2]);
			detail.put("product_cr", row[3]);
			detail.put("product_am", row[4]);
			detail.put("contract_ct", row[5]);
			detail.put("product_un", row[6]);
			detail.put("detail_contract_ed", row[7]); // CONTRACTDETAIL의 contract_ed
			return detail;
		}).collect(Collectors.toList());

		result.put("details", details);

		return result;
	}

    @Transactional
    public void delete_ContractAndDetails(List<String> contractCds) {
        contractRepository.delete_CONTRACTDETAIL(contractCds); // 디테일 먼저 삭제 처리
        contractRepository.delete_CONTRACT(contractCds); // 그 후에 수주 데이터 삭제
    }
	
    
    
	// 거래처 필터 데이터 조회
	public List<Map<String, Object>> select_FILTERED_CONTRACT(ContractFilterRequest filterRequest) {
		
		List<Object[]> result;
		
		result = contractRepository.select_FILTERED_CONTRACT(filterRequest);
		
		return result.stream().map(row -> {
			Map<String, Object> contract = new HashMap<>();
			contract.put("contract_cd", row[0]);
			contract.put("account_cd", row[1]);
			contract.put("employee_cd", row[2]);
			contract.put("contract_ps", row[3]);
			contract.put("contract_sd", row[4]);
			contract.put("contract_ed", row[5]);
			contract.put("contract_am", row[6]);
			contract.put("contract_mn", row[7]);
			contract.put("contract_st", row[8]);
			contract.put("contract_rm", row[9]);
			contract.put("contract_wr", row[10]);
			contract.put("contract_wd", row[11]);
			contract.put("contract_mf", row[12]);
			contract.put("contract_md", row[13]);
			
			return contract;
			
		}).collect(Collectors.toList());
	}
    
    // 수주 상태 업데이트 실행
    @Transactional
    public void updateContractStatus() {
        System.out.println("수주 상태 업데이트 실행...");
        contractRepository.updateContractStatus();
        System.out.println("수주 상태 업데이트 완료!");
    }

    
    // 출하페이지 상세조회
	public Map<String, Object> get_CONTRACT_SHIPMENT(String contractCd) {
		Map<String, Object> result = new HashMap<>();

		// 수주 정보 조회
		List<Object[]> contractList = contractRepository.findContractShipmentByCd(contractCd);
		
		if (!contractList.isEmpty()) {
			Object[] row = contractList.get(0); // 단일 계약이므로 첫 번째 행 사용
			Map<String, Object> contract = new HashMap<>();
			contract.put("contract_cd", row[0]);
			contract.put("account_cd", row[1]);
			contract.put("contract_sd", row[2]);
			contract.put("contract_ed", row[3]);
			contract.put("contract_am", row[4]);
			contract.put("contract_st", row[5]);

			result.put("contract", contract);
		}

		// 상세 정보 조회
		List<Object[]> detailList = contractRepository.select_CONTRACTDETAIL_SHIPMENT(contractCd);
		List<Map<String, Object>> details = detailList.stream().map(row -> {
			Map<String, Object> detail = new HashMap<>();
			detail.put("contract_cd", row[0]);
			detail.put("product_cd", row[1]);
			detail.put("product_sz", row[2]);
			detail.put("product_cr", row[3]);
			detail.put("product_am", row[4]);
			detail.put("contract_ct", row[5]);
			detail.put("product_un", row[6]);
			detail.put("detail_contract_ed", row[7]); // CONTRACTDETAIL의 contract_ed
			detail.put("total_stock", row[8]); // 재고
			detail.put("can_ship", row[9]); // 출하여부
			return detail;
		}).collect(Collectors.toList());

		result.put("details", details);

		return result;
	}
    
    // 출하페이지 수주조회
	public List<Map<String, Object>> select_CONTRACT_SHIPMENT() {

		List<Object[]> result;

		result = contractRepository.select_CONTRACT_SHIPMENT();

		return result.stream().map(row -> {
			Map<String, Object> contract = new HashMap<>();
			contract.put("contract_cd", row[0]);
			contract.put("account_cd", row[1]);
			contract.put("employee_cd", row[2]);
			contract.put("contract_ps", row[3]);
			contract.put("contract_sd", row[4]);
			contract.put("contract_ed", row[5]);
			contract.put("contract_am", row[6]);
			contract.put("contract_mn", row[7]);
			contract.put("contract_st", row[8]);
			contract.put("contract_rm", row[9]);
			contract.put("contract_wr", row[10]);
			contract.put("contract_wd", row[11]);
			contract.put("contract_mf", row[12]);
			contract.put("contract_md", row[13]);

			return contract;

		}).collect(Collectors.toList());
	}
	
	// 거래처 필터 데이터 조회
	public List<Map<String, Object>> select_FILTERED_CONTRACT_SHIPMENT(ContractFilterRequest filterRequest) {
		
		List<Object[]> result;
		
		result = contractRepository.select_FILTERED_CONTRACT_SHIPMENT(filterRequest);
		
		return result.stream().map(row -> {
			Map<String, Object> contract = new HashMap<>();
			contract.put("contract_cd", row[0]);
			contract.put("account_cd", row[1]);
			contract.put("employee_cd", row[2]);
			contract.put("contract_ps", row[3]);
			contract.put("contract_sd", row[4]);
			contract.put("contract_ed", row[5]);
			contract.put("contract_am", row[6]);
			contract.put("contract_mn", row[7]);
			contract.put("contract_st", row[8]);
			contract.put("contract_rm", row[9]);
			contract.put("contract_wr", row[10]);
			contract.put("contract_wd", row[11]);
			contract.put("contract_mf", row[12]);
			contract.put("contract_md", row[13]);
			
			return contract;
			
		}).collect(Collectors.toList());
	}
	
	
    // 출하 정보 업데이트
	@Transactional
	public void updateContractShipment(String contractCd, String contractEd, String contractSt, List<Map<String, Object>> details, String updatedUser) {
	    // CONTRACT 테이블 업데이트
	    contractRepository.updateContractShipment(contractEd, contractSt, contractCd);

	    // CONTRACTDETAIL 테이블에도 같은 contract_ed 업데이트
	    contractRepository.updateContractDetailShipment(contractEd, contractCd);

	    // 각 제품의 출하 처리 및 재고 차감
	    for (Map<String, Object> detail : details) {
	        String itemCd = (String) detail.get("product_cd") + "-" + detail.get("product_sz") + "-" + detail.get("product_cr");
	        int shipmentQty = Integer.parseInt(detail.get("product_am").toString());

	        // 창고별 가용 재고 목록 조회 (창고, 구역별 재고를 모두 가져옴)
	        List<Map<String, Object>> availableStocks = contractRepository.findAvailableStockByItem(itemCd);

	        if (availableStocks.isEmpty()) {
	            throw new IllegalStateException("출고 실패: 해당 제품의 창고 정보 없음 (" + itemCd + ")");
	        }

	        int remainingQty = shipmentQty;

	        for (Map<String, Object> stock : availableStocks) {
	            if (remainingQty <= 0) break;

	            String warehouseCd = (String) stock.get("warehouse_cd");
	            String wareareaCd = (String) stock.get("warearea_cd");
	            int availableQty = ((Number) stock.get("stock_aq")).intValue();

	            int qtyToDeduct = Math.min(availableQty, remainingQty);

	            // 출고 처리
	            int updatedRows = contractRepository.reduceStockQuantity(itemCd, warehouseCd, wareareaCd, qtyToDeduct, updatedUser);
	            if (updatedRows > 0) {
	                remainingQty -= qtyToDeduct;
	            }
	        }

	        if (remainingQty > 0) {
	            throw new IllegalStateException("출고 실패: 재고 부족 (" + itemCd + ", 부족 " + remainingQty + "개)");
	        }
	    }
	}

	
}
