package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ApprovalService {

	private final ApprovalRepository approvalRepository;

	private final CommonRepository commonRepository;

	// 모든 결재 정보 조회
	public List<Approval> findAll() {

		return approvalRepository.findAll();
	}

	// 특정 사용자(employee_cd)에 맞는 결재 정보 조회
	public List<Approval> findApprovalsByEmployee(String employee_cd) {
		return approvalRepository.findByApprovalCd(employee_cd);
	}

	// 결재 현황 데이터를 맵으로 변환
	public List<Map<String, Object>> getApprovalData(EmployeeDetails employeeDetails) {
		String currentCd = employeeDetails.getEmployee_cd();
		String currentRole = employeeDetails.getEmployee_rl(); // 현재 사용자의 권한

		List<Approval> approvals;

		if (currentRole.contains("SYS_ADMIN")) {
			approvals = findAll(); // 모든 결재 정보를 가져옴
		} else {
			approvals = findApprovalsByEmployee(currentCd); // 특정 사용자 결재 정보만 가져옴
		}

		// Approval 데이터를 Map으로 변환
		return approvals.stream().map(approval -> {
			Map<String, Object> row = new HashMap<>();
			row.put("approval_cd", approval.getApproval_cd());
			row.put("approval_sd", approval.getApproval_sd());
			row.put("approval_ed", approval.getApproval_ed());
			row.put("approval_dv", approval.getApproval_dv());
			row.put("approval_tt", approval.getApproval_tt());
			row.put("approval_ct", approval.getApproval_ct());
			row.put("approval_wr", approval.getApproval_wr());
			row.put("approval_wd", approval.getApproval_wd());
			row.put("approval_mf", approval.getApproval_mf());
			row.put("approval_md", approval.getApproval_md());
			return row;
		}).collect(Collectors.toList());
	}
	
	public List<Map<String, Object>> getApprovalDataByUser(EmployeeDetails employeeDetails) {
	    String currentCd = employeeDetails.getEmployee_cd();
	    String currentRole = employeeDetails.getEmployee_rl();

	    List<Approval> approvals;

	    if (currentRole.contains("SYS_ADMIN")) {
	        // 관리자는 모든 요청 조회
	        approvals = approvalRepository.findAll();
	    } else {
	        // 작성자나 결재권자 기준으로 데이터 조회
	        approvals = approvalRepository.findByApprover(currentCd);
	    }

	    // Approval 데이터를 Map으로 변환
	    return approvals.stream().map(approval -> {
	        Map<String, Object> row = new HashMap<>();
	        row.put("approval_cd", approval.getApproval_cd());
	        row.put("approval_sd", approval.getApproval_sd());
	        row.put("approval_ed", approval.getApproval_ed());
	        row.put("approval_dv", approval.getApproval_dv());
	        row.put("approval_tt", approval.getApproval_tt());
	        row.put("approval_ct", approval.getApproval_ct());
	        row.put("approval_wr", approval.getApproval_wr());
	        row.put("approval_wd", approval.getApproval_wd());
	        row.put("approval_mf", approval.getApproval_mf());
	        row.put("approval_md", approval.getApproval_md());
	        row.put("approval_av", approval.getApproval_av());
	        return row;
	    }).collect(Collectors.toList());
	}

	
	@Transactional
	public void handleApprovalInsert(ApprovalDTO approvalDTO) {
		// 1. 휴가 신청서 삽입
		insert_APPROVAL(approvalDTO);

		// 2. 상태 처리
		processApprovalStatus(approvalDTO);
		log.info("processApprovalStatus 이후 av 값 : " + approvalDTO.getApproval_av());
		// 3. 변경된 상태를 데이터베이스에 저장
		Approval approval = approvalRepository.findById(approvalDTO.getApproval_cd())
				.orElseThrow(() -> new IllegalArgumentException("Approval not found: " + approvalDTO.getApproval_cd()));
		log.info("1111111");
		// DTO 값을 엔티티에 반영
		Approval.setEmployeeEntity(approval, approvalDTO);

		log.info("222222222");
		// 상태가 반영된 엔티티 저장
		approvalRepository.save(approval);
	}

	// 직원 등록
	@Transactional
	public void insert_APPROVAL(ApprovalDTO approvalDTO) {

		String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

		// 현재 시간 가져오기 (초 단위까지)
		Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());

		// 작성자 처리
		approvalDTO.setApproval_mf(currentUserId);
		approvalDTO.setApproval_wr(currentUserId);

		// 작성일 처리
		approvalDTO.setApproval_wd(currentTime);
		approvalDTO.setApproval_md(currentTime);

		// 시퀀스 값 가져오기
		Long sequenceValue = approvalRepository.getNextSequenceValue();
		System.out.println("Next Sequence Value: " + sequenceValue);

		Approval approval = Approval.createApproval(approvalDTO, sequenceValue);

		approvalRepository.save(approval);

		approvalDTO.setApproval_cd(approval.getApproval_cd());

		System.out.println("@@@@@@@@@" + approval);
	}

	private void processApprovalStatus(ApprovalDTO approvalDTO) {
		// 오늘 날짜
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// approval_ct(JSON)에서 startday 값 추출
		Map<String, Object> approvalCt = approvalDTO.getApproval_ct(); // JSON 형태로 들어옴
		String startday = (String) approvalCt.get("startday");

		if (startday == null || startday.isEmpty()) {
			throw new IllegalArgumentException("startday 값이 누락되었습니다.");
		}

		// 공통코드 조회
		List<Common_codeDTO> approvalCodes = commonRepository.select_COMMON_list("APPROVAL");

		// 상태 값 결정
		String status = today.compareTo(startday) < 0 ? "10" // 대기
				: today.equals(startday) ? "20" // 1차 결재중
						: null;

		if (status == null) {
			throw new IllegalStateException("올바른 approval_av 상태를 결정할 수 없습니다.");
		}

		// 상태 이름 가져오기 (로깅 용도)
		String statusName = approvalCodes.stream().filter(code -> code.getCommon_cc().equals(status))
				.map(Common_codeDTO::getCommon_nm).findFirst()
				.orElseThrow(() -> new IllegalStateException("공통코드에서 상태 이름을 찾을 수 없습니다."));

		log.info("결정된 approval_av 상태: " + status + " (" + statusName + ")");

		// approvalDTO에 상태 저장
		approvalDTO.setApproval_av(status);
	}

//	 직원 업데이트
	public void update_APPROVAL(ApprovalDTO approvalDTO) {
		// 직원 조회
		Approval approval = approvalRepository.findById(approvalDTO.getApproval_cd())
				.orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다."));

		// 엔티티 업데이트
		approval.setEmployeeEntity(approval, approvalDTO);

		// 데이터베이스 저장
		approvalRepository.save(approval);
	}

	// 결재 삭제
	public void delete_APPROVAL(List<String> cds) {
		approvalRepository.deleteAllById(cds);

	}

	// 기안서 구분 데이터 조회
	public List<Common_code> selectCommonList(String string) {
		return commonRepository.selectCommonList("00", string);
	}

	// 직원 코드로 데이터 조회
	public List<Approval> findByApprovalCd(String currentCd) {
		return approvalRepository.findByApprovalCd(currentCd);
	}

	public List<Map<String, Object>> getFirstApproverListWithDeptAndPosition(String employeeGd) {
		// 1. COMMON_CODE에서 DEPARTMENT와 POSITION 데이터 가져오기
		List<Common_codeDTO> departments = commonRepository.select_COMMON_list("DEPARTMENT");
		List<Common_codeDTO> positions = commonRepository.select_COMMON_list("POSITION");

		// 2. Employee 데이터 가져오기
		List<Employee> employees = approvalRepository.findFirstApprovers(employeeGd);

		// 3. Employee 데이터와 COMMON_CODE 데이터 매핑
		return employees.stream().map(employee -> {
			// 부서명 매핑
			String departmentName = departments.stream()
					.filter(department -> department.getCommon_cc().equals(employee.getEmployee_dp()))
					.map(Common_codeDTO::getCommon_nm).findFirst().orElse("미지정");

			// 직위명 매핑
			String positionName = positions.stream()
					.filter(position -> position.getCommon_cc().equals(employee.getEmployee_gd()))
					.map(Common_codeDTO::getCommon_nm).findFirst().orElse("미지정");

			// Map으로 변환하여 반환
			Map<String, Object> result = new HashMap<>();
			result.put("employee_cd", employee.getEmployee_cd());
			result.put("employee_nm", employee.getEmployee_nm());
			result.put("department_nm", departmentName); // 부서명
			result.put("position_nm", positionName); // 직위명
			return result;
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> getSecondApproverListWithDeptAndPosition(String approval_fa) {
		// 1. COMMON_CODE에서 DEPARTMENT와 POSITION 데이터 가져오기
		List<Common_codeDTO> departments = commonRepository.select_COMMON_list("DEPARTMENT");
		List<Common_codeDTO> positions = commonRepository.select_COMMON_list("POSITION");

		// 2. Employee 데이터 가져오기
		List<Employee> employees = approvalRepository.findSecondApprovers(approval_fa);

		// 3. Employee 데이터와 COMMON_CODE 데이터 매핑
		return employees.stream().map(employee -> {
			// 부서명 매핑
			String departmentName = departments.stream()
					.filter(department -> department.getCommon_cc().equals(employee.getEmployee_dp()))
					.map(Common_codeDTO::getCommon_nm).findFirst().orElse("미지정");

			// 직위명 매핑
			String positionName = positions.stream()
					.filter(position -> position.getCommon_cc().equals(employee.getEmployee_gd()))
					.map(Common_codeDTO::getCommon_nm).findFirst().orElse("미지정");

			// Map으로 변환하여 반환
			Map<String, Object> result = new HashMap<>();
			result.put("employee_cd", employee.getEmployee_cd());
			result.put("employee_nm", employee.getEmployee_nm());
			result.put("department_nm", departmentName); // 부서명
			result.put("position_nm", positionName); // 직위명
			return result;
		}).collect(Collectors.toList());
	}

	@Transactional
	public void updateApprovalStatuses() {
		// 오늘 날짜
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// 오늘 날짜가 시작일(`approval_sd`)인 데이터 조회
		List<Approval> pendingApprovals = approvalRepository.findByApprovalSd(today);

		if (pendingApprovals.isEmpty()) {
			log.info("오늘 변경할 상태의 APPROVAL 데이터가 없습니다.");
			return;
		}

		log.info("오늘 업데이트할 결재 데이터 수: " + pendingApprovals.size());

		// 상태 업데이트
		for (Approval approval : pendingApprovals) {
			approval.setApproval_av("20"); // "1차 결재중" 상태로 변경
			approvalRepository.save(approval); // 업데이트
			log.info("Approval 상태 업데이트: " + approval.getApproval_cd());
		}

		log.info("상태 업데이트 완료");
	}

//    public List<Approval> select_FILTERED_APPROVAL(APPROVALFilterRequest filterRequest) {
//    	return approvalRepository.select_FILTERED_APPROVAL(filterRequest);
//    }

}
