package com.itwillbs.c4d2412t3p1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.TransferDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Transfer;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.TransferRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class TransferService {

	private final TransferRepository transferRepository;
	private final CommonRepository commonRepository;

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
			return transfer;
		}).collect(Collectors.toList());
	}

	public void insert_TRANSFER(TransferDTO transferDTO) {

		Transfer transfer = Transfer.setTransferEntity(transferDTO);

		transfer.setTransfer_aw("대기");
		transfer.setTransfer_wd("1");
		transfer.setTransfer_wr("1");
		transfer.setTransfer_md("1");
		transfer.setTransfer_mf("1");

		transferRepository.save(transfer);

	}

	public void delete_TRANSFER(List<Long> ids) {

		transferRepository.deleteAllById(ids);

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
}
