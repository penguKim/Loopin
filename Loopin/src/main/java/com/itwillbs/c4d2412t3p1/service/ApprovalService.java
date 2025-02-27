package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	public List<Map<String, Object>> getApprovalsForTab(String tabType) {
	    // 현재 로그인한 사용자의 ID와 사원번호 가져오기
	    EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String currentId = employeeDetails.getUsername(); // 로그인 ID
	    String currentCd = employeeDetails.getEmployee_cd(); // 사원번호

	    // 공통 코드 조회
	    Map<String, String> draftCodes = commonRepository.select_COMMON_list("DRAFT").stream()
	            .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));

	    Map<String, String> approvalCodes = commonRepository.select_COMMON_list("APPROVAL").stream()
	            .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
	    
	    List<Approval> approvals;

	    if ("receive".equals(tabType)) {
	        approvals = approvalRepository.findByApprover(currentCd);
	    } else if ("send".equals(tabType)) {
	        approvals = approvalRepository.findByApprovalCd(currentId);
	    } else {
	        throw new IllegalArgumentException("Invalid tab type");
	    }

	    // approval_wr (로그인 ID 기반) & approval_fa, approval_sa (사원번호 기반) 조회를 위해 Set 분리
	    Set<String> employeeIds = new HashSet<>();
	    Set<String> employeeCds = new HashSet<>();

	    approvals.forEach(approval -> {
	        if (approval.getApproval_wr() != null) employeeIds.add(approval.getApproval_wr()); // 로그인 ID
	        if (approval.getApproval_fa() != null) employeeCds.add(approval.getApproval_fa()); // 사원번호
	        if (approval.getApproval_sa() != null) employeeCds.add(approval.getApproval_sa()); // 사원번호
	    });

	    // 사원번호(employee_cd) → 사원명 변환
	    Map<String, String> employeeMapByCd = approvalRepository.findEmployeesByEmployeeCd(employeeCds).stream()
	            .collect(Collectors.toMap(emp -> emp.getEmployee_cd(), emp -> emp.getEmployee_nm() + "(" + emp.getEmployee_cd() + ")"));

	    // 로그인 ID(employee_id) → 사원명 변환
	    Map<String, String> employeeMapById = approvalRepository.findEmployeesByEmployeeId(employeeIds).stream()
	            .collect(Collectors.toMap(emp -> emp.getEmployee_id(), emp -> emp.getEmployee_nm() + "(" + emp.getEmployee_cd() + ")"));

	    // Approval 데이터를 Map으로 변환
	    return approvals.stream().map(approval -> {
	        Map<String, Object> row = new HashMap<>();
	        row.put("approval_cd", approval.getApproval_cd());
	        row.put("approval_sd", approval.getApproval_sd());
	        row.put("approval_ed", approval.getApproval_ed());
	        row.put("approval_ct", approval.getApproval_ct());

	        // 기안서 구분(DRAFT) 공통 코드 이름으로 변환
	        String draftName = draftCodes.getOrDefault(approval.getApproval_dv(), "Unknown");
	        row.put("approval_dv", draftName);
	        row.put("approval_dv_cc", approval.getApproval_dv());

	        // 결재 상태(APPROVAL) 공통 코드 이름으로 변환
	        String approvalName = approvalCodes.getOrDefault(approval.getApproval_av(), "Unknown");
	        row.put("approval_av", approvalName);
	        row.put("approval_av_cc", approval.getApproval_av()); // 공통 코드 값 추가

	        row.put("approval_tt", approval.getApproval_tt());

	        // approval_wr (로그인 ID → 사원명 + 사원번호 변환)
	        row.put("approval_wr", employeeMapById.getOrDefault(approval.getApproval_wr(), approval.getApproval_wr()));

	        // approval_fa (1차 결재권자 사원번호 → 사원명 + 사원번호 변환)
	        row.put("approval_fa_parse", employeeMapByCd.getOrDefault(approval.getApproval_fa(), approval.getApproval_fa()));
	        row.put("approval_fa", approval.getApproval_fa());
	        
	        // approval_sa (2차 결재권자 사원번호 → 사원명 + 사원번호 변환)
	        row.put("approval_sa_parse", employeeMapByCd.getOrDefault(approval.getApproval_sa(), approval.getApproval_sa()));
	        row.put("approval_sa", approval.getApproval_sa());

	        // 현재 사용자가 1차 결재권자인지, 2차 결재권자인지 추가
	        row.put("is_first_approver", currentCd.equals(approval.getApproval_fa())); // 1차 여부
	        row.put("is_second_approver", currentCd.equals(approval.getApproval_sa())); // 2차 여부

	        return row;
	    }).collect(Collectors.toList());
	}


	@Transactional
	public void handleApproval(ApprovalDTO approvalDTO) {
		// 1. 휴가 신청서 삽입
		if (approvalDTO.getApproval_cd() == null) {
			insert_APPROVAL(approvalDTO);
		} else {
			update_APPROVAL(approvalDTO);
		}

		// 2. 상태 처리
		processApprovalStatus(approvalDTO);
		log.info("processApprovalStatus 이후 av 값 : " + approvalDTO.getApproval_av());
		// 3. 변경된 상태를 데이터베이스에 저장
		Approval approval = approvalRepository.findById(approvalDTO.getApproval_cd())
				.orElseThrow(() -> new IllegalArgumentException("Approval not found: " + approvalDTO.getApproval_cd()));
		// DTO 값을 엔티티에 반영
		Approval.setEmployeeEntity(approval, approvalDTO);

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

		// approval_sd(결재 시작일) 값 추출
		String approvalStartDate = approvalDTO.getApproval_sd();

		if (approvalStartDate == null || approvalStartDate.isEmpty()) {
			throw new IllegalArgumentException("approval_sd 값이 누락되었습니다.");
		}

		// 공통코드 조회
		List<Common_codeDTO> approvalCodes = commonRepository.select_COMMON_list("APPROVAL");

		// 상태 값 결정
		String status = today.compareTo(approvalStartDate) < 0 ? "10" // 대기
				: today.equals(approvalStartDate) ? "20" // 1차 결재중
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

	public Employee getEmployeeByCd(String employeeCd) {
		// 사원번호로 Employee 정보 조회
		return approvalRepository.findByEmployeeCd(employeeCd)
				.orElseThrow(() -> new IllegalArgumentException("사원 정보를 찾을 수 없습니다: " + employeeCd));
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

	@Transactional
	public void processApproval(String approval_cd, String currentUserCd, boolean isApproved)
			throws IllegalAccessException {
		// Approval 엔티티 가져오기
		Approval approval = approvalRepository.findById(approval_cd)
				.orElseThrow(() -> new IllegalArgumentException("결재를 찾을 수 없습니다."));

		// 현재 사용자가 1차 결재권자인지, 2차 결재권자인지 확인
		boolean isFirstApprover = currentUserCd.equals(approval.getApproval_fa());
		boolean isSecondApprover = currentUserCd.equals(approval.getApproval_sa());

		if (!isFirstApprover && !isSecondApprover) {
			throw new IllegalAccessException("사용자는 결재권자가 아닙니다.");
		}

		if (isFirstApprover) {
			// 1차 결재권자
			if (isApproved) {
				approval.setApproval_av("30"); // 승인 상태: 1차 -> 2차 결재로
			} else {
				approval.setApproval_av("50"); // 반려 상태
			}
		} else if (isSecondApprover) {
			// 2차 결재권자
			if (isApproved) {
				approval.setApproval_av("40"); // 최종 승인 상태
			} else {
				approval.setApproval_av("50"); // 반려 상태
			}
		}

		approvalRepository.save(approval); // 상태 업데이트
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
	
	public Map<String, Object> getApprovalDataForPdf(String approval_cd) {
	    // 1. Approval 엔티티 조회
	    Approval approval = approvalRepository.findById(approval_cd)
	            .orElseThrow(() -> new RuntimeException("Approval not found: " + approval_cd));

	    // 2. approval_ct(JSON 문자열)을 Map으로 파싱
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> approvalCtMap;
	    try {
	        approvalCtMap = mapper.readValue(approval.getApproval_ct(), new TypeReference<Map<String, Object>>() {});
	    } catch (IOException e) {
	        throw new RuntimeException("Error parsing approval_ct JSON", e);
	    }

	    // 3. 휴가 신청서의 경우, approvalCtMap에서 "annualtype" 값을 추출 후 공통코드 "ANNUAL"을 통해 변환
	    String annualTypeCode = (String) approvalCtMap.get("annualtype");
	    Map<String, String> annualCodes = commonRepository.select_COMMON_list("ANNUAL")
	            .stream()
	            .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
	    String annualTypeName = annualCodes.getOrDefault(annualTypeCode, "Unknown");

	    // 4. 기안서 구분(approval_dv): 공통코드 "DRAFT"를 통해 변환
	    Map<String, String> draftCodes = commonRepository.select_COMMON_list("DRAFT")
	            .stream()
	            .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
	    String draftName = draftCodes.getOrDefault(approval.getApproval_dv(), "Unknown");

	    // 5. 결재 상태(approval_av): 공통코드 "APPROVAL"을 통해 변환
	    Map<String, String> approvalCodes = commonRepository.select_COMMON_list("APPROVAL")
	            .stream()
	            .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
	    String approvalStatusName = approvalCodes.getOrDefault(approval.getApproval_av(), "Unknown");

	    // 6. 1차 결재권자(approval_fa) 변환: 사원번호 → "사원명 직급"
	    String firstApproverInfo = "N/A";
	    if (approval.getApproval_fa() != null && !approval.getApproval_fa().isEmpty()) {
	        try {
	            Employee firstApprover = getEmployeeByCd(approval.getApproval_fa());
	            firstApproverInfo = transformEmployeeInfo(firstApprover);
	        } catch (Exception e) {
	            firstApproverInfo = approval.getApproval_fa();
	        }
	    }

	    // 7. 2차 결재권자(approval_sa) 변환: 사원번호 → "사원명 직급"
	    String secondApproverInfo = "N/A";
	    if (approval.getApproval_sa() != null && !approval.getApproval_sa().isEmpty()) {
	        try {
	            Employee secondApprover = getEmployeeByCd(approval.getApproval_sa());
	            secondApproverInfo = transformEmployeeInfo(secondApprover);
	        } catch (Exception e) {
	            secondApproverInfo = approval.getApproval_sa();
	        }
	    }

	    // 8. 기안자(approval_wr) 변환: employee_id 기반 → "사원명 직급"
	    String draftWriter = "N/A";
	    if (approval.getApproval_wr() != null && !approval.getApproval_wr().isEmpty()) {
	        try {
	            Employee drafter = getEmployeeByEmployeeId(approval.getApproval_wr());
	            draftWriter = transformEmployeeInfo(drafter);
	        } catch (Exception e) {
	            draftWriter = approval.getApproval_wr();
	        }
	    }

	    // 9. 최종적으로 PDF 템플릿에 전달할 데이터를 Map에 구성
	    Map<String, Object> pdfData = new HashMap<>();
	    pdfData.put("approval", approval);
	    pdfData.put("approvalCt", approvalCtMap);
	    pdfData.put("annualTypeName", annualTypeName);
	    pdfData.put("draftName", draftName);
	    pdfData.put("approvalStatusName", approvalStatusName);
	    pdfData.put("firstApproverInfo", firstApproverInfo);
	    pdfData.put("secondApproverInfo", secondApproverInfo);
	    pdfData.put("draftWriter", draftWriter);

	    return pdfData;
	}

//	 기안자(approval_wr)가 employee_id 형태이므로, 해당 employee_id를 이용해 Employee 정보를 조회합니다.
//	 (ApprovalRepository에 해당 메소드가 정의되어 있어야 합니다.)
	public Employee getEmployeeByEmployeeId(String employeeId) {
	    return approvalRepository.findByEmployeeId(employeeId)
	            .orElseThrow(() -> new IllegalArgumentException("Employee not found for id: " + employeeId));
	}

//	 Employee 객체를 받아 "사원명 직급" 형태의 문자열로 변환합니다.
//	 직위는 공통코드 "POSITION"을 통해 매핑됩니다.
	private String transformEmployeeInfo(Employee employee) {
	    Map<String, String> positionCodes = commonRepository.select_COMMON_list("POSITION")
	            .stream()
	            .collect(Collectors.toMap(Common_codeDTO::getCommon_cc, Common_codeDTO::getCommon_nm));
	    String positionName = positionCodes.getOrDefault(employee.getEmployee_gd(), "미지정");
	    return employee.getEmployee_nm() + " " + positionName;
	}


//    public List<Approval> select_FILTERED_APPROVAL(APPROVALFilterRequest filterRequest) {
//    	return approvalRepository.select_FILTERED_APPROVAL(filterRequest);
//    }

}
