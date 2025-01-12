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

	public List<Map<String, Object>> select_TRANSFER_DETAIL() {

		List<Object[]> result = transferRepository.findAllWithDetails();

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
	private void processTransferIfToday(TransferDTO transferDTO) {
        // 오늘 날짜 확인
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (transferDTO.getTransfer_ad().equals(today)) {
            // 오늘 날짜라면 EMPLOYEE 업데이트 및 TRANSFER_AW 수정 처리
        	log.info("Processing transfer for ID: " + transferDTO.getTransfer_id());
            processTransferForToday(
                transferDTO.getTransfer_ad(),
                transferDTO.getEmployee_cd(),
                transferDTO.getTransfer_adp(),
                transferDTO.getTransfer_ag(),
                transferDTO.getTransfer_id()
            );
        }
    }

//	인사발령날짜가 오늘날짜인 경우의 변경
	public void processTransferForToday(String transfer_ad, String employee_cd, String transfer_adp, String transfer_ag, Long transfer_id) {
        // EMPLOYEE 테이블 업데이트
        transferRepository.updateEmployeeDepartmentAndGrade(employee_cd, transfer_adp, transfer_ag);

        // TRANSFER_AW 업데이트
        transferRepository.updateTransfer_aw(transfer_id);
    }

//	스케줄러작업 - 인사발령날짜가 오늘날짜가 아닌경우의 변경
	@Transactional
	public void updatePendingTransfers() {
		log.info("UPDATE 작업 시작: TRANSFER 및 EMPLOYEE 테이블");

		// 오늘 날짜를 YYYY-MM-DD 형식으로 가져오기
		String today = java.time.LocalDate.now().toString();

		try {
			// EMPLOYEE 테이블 업데이트 및 TRANSFER_AW 업데이트
			transferRepository.updateTransferAndEmployee(today);
			transferRepository.updateTransfer_aw(today); // transfer_aw 업데이트

			log.info("TRANSFER 및 EMPLOYEE 테이블 업데이트 성공");
		} catch (Exception e) {
			log.log(java.util.logging.Level.SEVERE, "TRANSFER 및 EMPLOYEE 업데이트 중 오류 발생", e);
			throw e; // 오류를 다시 던짐
		}
	}

	// 모달 부서코드 가져오기
	public List<Common_code> selectDeptList(String string) {
		return commonRepository.selectDeptList("00", string);
	}

	// 모달 직급코드 가져오기
	public List<Common_code> selectGradeList(String string) {
		return commonRepository.selectGradeList("00", string);
	}

	public List<Common_code> selectTRTypeList(String string) {
		return commonRepository.selectGradeList("00", string);
	}

	public List<Common_code> selectDPTypeList(String string) {
		return commonRepository.selectGradeList("00", string);
	}

}
