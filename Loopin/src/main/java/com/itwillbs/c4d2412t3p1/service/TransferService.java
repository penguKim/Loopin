package com.itwillbs.c4d2412t3p1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.TransferDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Transfer;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.TransferRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class TransferService {

	private final TransferRepository transferRepository;
	private final CommonRepository commonRepository;
	private final EmployeeRepository employeeRepository;

	public List<Map<String, Object>> select_TRANSFER_DETAIL(String role, String employee_cd) {
		List<Object[]> result;

		if ("SYS_ADMIN".equals(role) || "developer".equals(role)) {
			// 전체 조회
			result = transferRepository.findAllWithDetails();
		} else if ("employee".equals(role)) {
			// 본인 데이터만 조회
			result = transferRepository.findAllWithDetailsByEmployeeCd(employee_cd);
		} else {
			throw new IllegalArgumentException("Invalid role: " + role);
		}

		return result.stream().map(row -> {
			Map<String, Object> transfer = new HashMap<>();
			transfer.put("transfer_id", row[0]);
			transfer.put("employee_cd", row[1]);
			transfer.put("employee_nm", row[2]);
			transfer.put("transfer_ad", row[3]);
			transfer.put("transfer_ac", row[4]);
			transfer.put("transfer_og", row[5]);
			transfer.put("transfer_ag", row[6]);
			transfer.put("transfer_od", row[7]);
			transfer.put("transfer_adp", row[8]);
			transfer.put("transfer_aw", row[9]);
			transfer.put("transfer_mg", row[10]);
			return transfer;
		}).collect(Collectors.toList());
	}

	public void insert_TRANSFER(TransferDTO transferDTO) {

		Transfer transfer = Transfer.setTransferEntity(transferDTO);

		transfer.setTransfer_aw(false);
		transfer.setTransfer_wd("1");
		transfer.setTransfer_wr("1");
		transfer.setTransfer_md("1");
		transfer.setTransfer_mf("1");

		Transfer savedTransfer = transferRepository.save(transfer);

		transferDTO.setTransfer_id(savedTransfer.getTransfer_id());

	}

	public void delete_TRANSFER(List<Long> ids) {

		transferRepository.deleteAllById(ids);

	}

	public List<Map<String, Object>> select_EMPLOYEE_COMMON() {

		List<Object[]> result = transferRepository.findEmployeeList();

		return result.stream().map(row -> {
			Map<String, Object> employee = new HashMap<>();
			employee.put("employee_cd", row[0]);
			employee.put("employee_nm", row[1]);
			employee.put("department_name", row[2]);
			employee.put("position_name", row[3]);
			return employee;
		}).collect(Collectors.toList());

	}

	public Map<String, Object> select_DEPARTMENT_MANAGER(String transfer_adp) {
		return transferRepository.findDepartmentManager(transfer_adp);
	}

	@Transactional
	public void handleTransferInsert(TransferDTO transferDTO) {
		// TRANSFER INSERT 작업
		insert_TRANSFER(transferDTO);

		// 오늘 날짜 확인 및 추가 작업
		processTransferIfToday(transferDTO);
	}

	@Transactional
	public void handleTransferUpdate(TransferDTO transferDTO) {
		// 1. 데이터 업데이트 처리
		update_TRANSFER(transferDTO);

		// 2. 오늘 날짜 확인 및 추가 작업
		processTransferIfToday(transferDTO);
	}

	@Transactional
	public void update_TRANSFER(TransferDTO transferDTO) {
		// 1. 기존 데이터 조회
		List<Object[]> result = transferRepository.findAllWithDetailsByEmployeeCd(transferDTO.getEmployee_cd());
		if (result.isEmpty()) {
			throw new IllegalArgumentException("해당 employee_cd에 대한 데이터가 없습니다.");
		}

		// 2. 조회된 데이터를 Map으로 변환
		Object[] row = result.get(0);
		Map<String, Object> existingData = new HashMap<>();
		existingData.put("transfer_id", row[0]);
		existingData.put("employee_cd", row[1]);
		existingData.put("employee_nm", row[2]);
		existingData.put("transfer_ad", row[3]);
		existingData.put("transfer_ac", row[4]);
		existingData.put("transfer_og", row[5]);
		existingData.put("transfer_ag", row[6]);
		existingData.put("transfer_od", row[7]);
		existingData.put("transfer_adp", row[8]);
		existingData.put("transfer_aw", row[9]); // 수정하지 않을 필드 그대로 유지

		// 3. 기존 데이터와 DTO 데이터 병합
		Transfer transfer = Transfer.setTransferEntity(transferDTO);
		transfer.setTransfer_id((Long) existingData.get("transfer_id"));
		// transfer_aw 값은 수정하지 않고 기존 값을 그대로 둠

		// 추가 속성 설정
		transfer.setTransfer_wd("1");
		transfer.setTransfer_wr("1");
		transfer.setTransfer_md("1");
		transfer.setTransfer_mf("1");

		// 4. 데이터 저장
		Transfer savedTransfer = transferRepository.save(transfer);

		// 5. transferDTO에 저장된 ID 설정
		transferDTO.setTransfer_id(savedTransfer.getTransfer_id());
	}

	private void processTransferIfToday(TransferDTO transferDTO) {
		// 오늘 날짜 확인
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if (transferDTO.getTransfer_ad().equals(today)) {
			// 오늘 날짜라면 EMPLOYEE 업데이트 및 TRANSFER_AW 수정 처리
			log.info("Processing transfer for ID: " + transferDTO.getTransfer_id());
			processTransferForToday(transferDTO.getTransfer_ad(), transferDTO.getEmployee_cd(),
					transferDTO.getTransfer_adp(), transferDTO.getTransfer_ag(), transferDTO.getTransfer_id(),
					transferDTO.getTransfer_mg());
		}
	}

	// 인사발령날짜가 오늘날짜인 경우의 변경
	public void processTransferForToday(String transfer_ad, String employee_cd, String transfer_adp, String transfer_ag,
			Long transfer_id, Boolean transfer_mg) {
		// 1. 기존 부서장 확인 및 부서원으로 변경
		if (transfer_mg) { // 발령된 직원이 부서장인 경우
			Map<String, Object> existingManager = transferRepository.findDepartmentManager(transfer_adp);

			if (existingManager != null && existingManager.get("employee_cd") != null) {
				String existingManagerCd = (String) existingManager.get("employee_cd");

				// 기존 부서장의 employee_mg 값을 false로 변경
				transferRepository.updateEmployeeManager(existingManagerCd, false); // 기존 부서장 -> 부서원

				log.info("기존 부서장(" + existingManagerCd + ")을 부서원으로 변경했습니다.");
			} else {
				log.info("기존 부서장이 없습니다. 부서원 변경 작업을 건너뜁니다.");
			}
		}

		// 2. EMPLOYEE 테이블 업데이트 (발령된 직원 업데이트)
		transferRepository.updateEmployeeDepartmentAndGrade(employee_cd, transfer_adp, transfer_ag, transfer_mg);
		log.info("발령된 직원(" + employee_cd + ")을 부서장으로 설정했습니다.");

		// 3. TRANSFER_AW 업데이트
		transferRepository.updateTransfer_aw(transfer_id);
		log.info("TRANSFER_AW가 업데이트되었습니다.");
	}

//	스케줄러작업 - 인사발령날짜가 오늘날짜가 아닌경우의 변경
	@Transactional
	public void updatePendingTransfers() {
		log.info("스케줄러: TRANSFER 및 EMPLOYEE 테이블 업데이트 시작");

		// 오늘 날짜를 YYYY-MM-DD 형식으로 가져오기
		String today = java.time.LocalDate.now().toString();

		try {
			// 1. TRANSFER 테이블에서 오늘 날짜와 관련된 데이터 가져오기
			List<Transfer> transfers = transferRepository.findTransfersByDate(today); // 오늘 날짜의 인사발령 데이터 조회

			for (Transfer transfer : transfers) {
				String employee_cd = transfer.getEmployee_cd();
				String transfer_adp = transfer.getTransfer_adp();
				String transfer_ag = transfer.getTransfer_ag();
				Boolean transfer_mg = transfer.getTransfer_mg();

				// 2. 부서장이 발령되는 경우, 기존 부서장 처리
				if (transfer_mg) {
					Map<String, Object> existingManager = transferRepository.findDepartmentManager(transfer_adp);
					if (existingManager != null) {
						String existingManagerCd = (String) existingManager.get("employee_cd");
						// 기존 부서장을 부서원으로 변경
						transferRepository.updateEmployeeManager(existingManagerCd, false);
						log.info("기존 부서장(" + existingManagerCd + ")을 부서원으로 변경했습니다.");
					}
				}

				// 3. 발령된 직원 업데이트 (EMPLOYEE 테이블 업데이트)
				transferRepository.updateEmployeeDepartmentAndGrade(employee_cd, transfer_adp, transfer_ag,
						transfer_mg);
				log.info("발령된 직원(" + employee_cd + ")의 부서 및 직위를 업데이트했습니다.");

				// 4. TRANSFER_AW 업데이트
				transferRepository.updateTransfer_aw(transfer.getTransfer_id());
				log.info("TRANSFER_AW가 업데이트되었습니다. (TRANSFER ID: " + transfer.getTransfer_id() + ")");
			}

			log.info("TRANSFER 및 EMPLOYEE 테이블 업데이트 성공");
		} catch (Exception e) {
			log.log(java.util.logging.Level.SEVERE, "TRANSFER 및 EMPLOYEE 업데이트 중 오류 발생", e);
			throw e; // 오류를 다시 던짐
		}
	}

	// 공통데이터 가져오기
	public List<Common_code> selectCommonList(String string) {
		return commonRepository.selectCommonList("00", string);
	}

}
