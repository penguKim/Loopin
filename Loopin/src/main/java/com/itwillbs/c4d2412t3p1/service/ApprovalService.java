package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ApprovalService {

	private final ApprovalRepository approvalRepository;

	private final CommonRepository commonRepository;

	// 결재 현황 조회
	public List<Approval> findAll() {

		return approvalRepository.findAll();
	}

	// 직원 등록
	@Transactional
	public void insert_APPROVAL(ApprovalDTO approvalDTO) throws IOException {

		// 시퀀스 값 가져오기
		Long sequenceValue = approvalRepository.getNextSequenceValue();
		System.out.println("Next Sequence Value: " + sequenceValue);

		Approval approval = Approval.createApproval(approvalDTO, sequenceValue);

		approvalRepository.save(approval);
		System.out.println("@@@@@@@@@" + approval);
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

	// ID로 Approval 조회
	public Approval findEmployeeById(String approval_cd) {
		// Repository를 사용하여 데이터 조회
		return approvalRepository.findById(approval_cd)
				.orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
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
	            .map(Common_codeDTO::getCommon_nm)
	            .findFirst()
	            .orElse("미지정");

	        // 직위명 매핑
	        String positionName = positions.stream()
	            .filter(position -> position.getCommon_cc().equals(employee.getEmployee_gd()))
	            .map(Common_codeDTO::getCommon_nm)
	            .findFirst()
	            .orElse("미지정");

	        // Map으로 변환하여 반환
	        Map<String, Object> result = new HashMap<>();
	        result.put("employee_cd", employee.getEmployee_cd());
	        result.put("employee_nm", employee.getEmployee_nm());
	        result.put("department_nm", departmentName); // 부서명
	        result.put("position_nm", positionName);     // 직위명
	        return result;
	    }).collect(Collectors.toList());
	}


//    public List<Approval> select_FILTERED_APPROVAL(APPROVALFilterRequest filterRequest) {
//    	return approvalRepository.select_FILTERED_APPROVAL(filterRequest);
//    }

}
