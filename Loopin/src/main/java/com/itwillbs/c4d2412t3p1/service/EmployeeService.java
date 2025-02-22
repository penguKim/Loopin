package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.LogFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EmployeeFilterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class EmployeeService {

	private final EmployeeRepository EmployeeRepository;

	private final CommonRepository commonRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Autowired
	@Value("${file.upload-dir}")
	private String uploadDir;

	// 직원 조회
	public List<Employee> findAll() {
		
		return EmployeeRepository.findAll();
	}


	// 직원 등록
	@Transactional
    public void insert_EMPLOYEE(EmployeeDTO employeeDTO, MultipartFile employee_pi) throws IOException {
        String fileName = null;

        
        // 파일 저장 처리
        if (employee_pi != null && !employee_pi.isEmpty()) {
        	fileName = UUID.randomUUID().toString() + "_" + employee_pi.getOriginalFilename();
        	String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        	Path path = Paths.get(uploadDir, "EMPLOYEE", dateDir, fileName);
        
	        // 업로드 디렉터리 생성
	        if (!Files.exists(path.getParent())) {
	        	Files.createDirectories(path.getParent());
	        }
	        
	        // 파일 저장
	        Files.copy(employee_pi.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        
        
        
        // 시퀀스 값 가져오기
        Long sequenceValue = EmployeeRepository.getNextSequenceValue();
        System.out.println("Next Sequence Value: " + sequenceValue);

        // Employee 엔티티 생성
        Employee employee = Employee.createEmployee(employeeDTO, fileName, sequenceValue, passwordEncoder);
        // EmployeeRepository.save() 호출 직전에 로그 추가
        System.out.println("Saving employee: " + employee);
        
        EmployeeRepository.save(employee);
        System.out.println("Employee saved: " + employee);
    }

    // 직원 삭제
	public void updateEmployeeStatus(List<String> employeeCds, Boolean status) {
		EmployeeRepository.updateEmployeeStatus(employeeCds, status);
		
	}

//	 직원 업데이트
	public void update_EMPLOYEE(EmployeeDTO employeeDTO, MultipartFile employee_pi) throws IOException {
	    // 직원 조회
	    Employee employee = EmployeeRepository.findById(employeeDTO.getEmployee_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다."));

	    // 기존 사진 삭제 처리
	    if ("true".equals(employeeDTO.getPhotoDeleted())) {
	        if (employee.getEmployee_pi() != null) {
	            String photoFileName = employee.getEmployee_pi();
	            Path photoPath = Paths.get(uploadDir, photoFileName);
	            Files.deleteIfExists(photoPath); // 기존 사진 파일 삭제
	            employee.setEmployee_pi(null); // 엔티티의 사진 경로 초기화
	        }
	    }

	    // 새 사진 업로드 처리
	    if (employee_pi != null && !employee_pi.isEmpty()) {
	        String fileName = UUID.randomUUID().toString() + "_" + employee_pi.getOriginalFilename();
	        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        Path path = Paths.get(uploadDir, "EMPLOYEE", dateDir, fileName);

	        // 업로드 디렉터리 생성
	        if (!Files.exists(path.getParent())) {
	            Files.createDirectories(path.getParent());
	        }

	        // 파일 저장
	        Files.copy(employee_pi.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	    } else if (!"true".equals(employeeDTO.getPhotoDeleted())) {
	        // 사진 삭제 요청이 없을 경우, 기존 사진 유지
	        employeeDTO.setEmployee_pi(employee.getEmployee_pi());
	    }

	    // 엔티티 업데이트
	    employee.setEmployeeEntity(employee, employeeDTO, passwordEncoder);

	    // 데이터베이스 저장
	    EmployeeRepository.save(employee);
	}

	
	public void deleteEmployeePhoto(String employee_cd) {
	    // 직원 조회
	    Employee employee = EmployeeRepository.findById(employee_cd)
	            .orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다. ID: " + employee_cd));

	    // 사진 파일명 확인
	    String photoFileName = employee.getEmployee_pi();
	    if (photoFileName != null && !photoFileName.isEmpty()) {
	        Path photoPath = Paths.get(uploadDir, photoFileName);
	        try {
	            Files.deleteIfExists(photoPath); // 파일 삭제
	            log.info("사진 삭제 완료: " + photoPath);
	        } catch (IOException e) {
	            log.warning("사진 삭제 실패: " + photoPath + " (" + e.getMessage() + ")");
	            throw new RuntimeException("사진 삭제 중 오류 발생: " + photoPath, e);
	        }
	    }

	    // 엔티티의 사진 정보 초기화
	    employee.setEmployee_pi(null);
	    EmployeeRepository.save(employee); // 변경 사항 저장
	}

    // ID로 Employee 조회
    public Employee findEmployeeById(String employee_cd) {
        // Repository를 사용하여 데이터 조회
        return EmployeeRepository.findById(employee_cd)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
    }
	
    
    // 공통코드 데이터 조회
    public List<Common_code> selectCommonList(String string) {
    	return commonRepository.selectCommonList("00", string);
    }

	
	
	// 성별 차트 조회
	public List<Map<String, Object>> getEmployeeGenderStatsByDate() {
		
		return EmployeeRepository.findEmployeeGenderStatsByDate();
	}

	// 부서별 인원 차트 조회
	public List<Map<String, Object>> getEmployeeDeptStatsByDate() {
		
		return EmployeeRepository.getEmployeeDeptStatsByDate();
	}

	// 직위별 인원 차트 조회
	public List<Map<String, Object>> getEmployeePosiStatsByDate() {
		
		return EmployeeRepository.getEmployeePosiStatsByDate();
	}
	

	// 입사퇴자 조회
	public Map<String, List<?>> getHireAndRetireStatsByDate(String startDate, String endDate) {
	    // 입사자 및 퇴사자 데이터 조회
	    List<String> categories = EmployeeRepository.findDistinctMonths(startDate, endDate); // 월별 카테고리 조회
	    List<Integer> hireData = EmployeeRepository.findHireCountsByMonth(startDate, endDate);  // 입사자 수
	    List<Integer> retireData = EmployeeRepository.findRetireCountsByMonth(startDate, endDate); // 퇴사자 수

	    // 결과 반환 (Integer 타입 그대로 사용)
	    Map<String, List<?>> result = new HashMap<>();
	    result.put("categories", categories);  // List<String>
	    result.put("hireData", hireData);  // List<Integer>
	    result.put("retireData", retireData);  // List<Integer>

	    return result;
	}

	
	// 직원 코드로 데이터 조회
	public List<Employee> findByEmployeeCd(String currentCd) {
		return EmployeeRepository.findByEmployeeCd(currentCd);
	}



    // 아이디 중복 여부 확인
    public boolean isEmployeeIdAvailable(String employee_id) {
    	return EmployeeRepository.existsByEmployeeId(employee_id) == 0;// 아이디 존재 여부 체크
    }

    public List<Map<String, Object>> select_EMPLOYEE_DETAIL(String type, String currentCd) {

        List<Object[]> result;
        
        if (type != null && type.equals("1")) {
            result = EmployeeRepository.findAllWithDetailsCd(currentCd);
        } else {
            result = EmployeeRepository.findAllWithDetails();
        }

        return result.stream().map(row -> {
            Map<String, Object> employee = new HashMap<>();
            employee.put("employee_cd", row[0]);
            employee.put("employee_id", row[1]);
            employee.put("employee_pw", row[2]);
            employee.put("employee_dp", row[3]);
            employee.put("employee_gd", row[4]);
            employee.put("employee_hd", row[5]);
            employee.put("employee_rd", row[6]);
            employee.put("employee_rr", row[7]);
            employee.put("employee_cg", row[8]);
            employee.put("employee_nt", row[9]);
            employee.put("employee_nm", row[10]);
            employee.put("employee_bd", row[11]);
            employee.put("employee_ad", row[12]);
            employee.put("employee_sb", row[13]);
            employee.put("employee_ph", row[14]);
            employee.put("employee_em", row[15]);
            employee.put("employee_pi", row[16]);
            employee.put("employee_bs", row[17]);
            employee.put("employee_bk", row[18]);
            employee.put("employee_an", row[19]);
            employee.put("employee_dt", row[20]);
            employee.put("employee_wr", row[21]);
            employee.put("employee_wd", row[22]);
            employee.put("employee_mf", row[23]);
            employee.put("employee_md", row[24]);
            employee.put("employee_mg", row[25]);
            employee.put("employee_rl", row[26]);
            employee.put("employee_us", row[27]);

            return employee;
        }).collect(Collectors.toList());
    }


	public List<Map<String, Object>> select_EMPLOYEE_DETAIL_CD(String currentCd) {

		
		List<Object[]> result;
		
		result = EmployeeRepository.findAllWithDetailsCd(currentCd);
		
		
		return result.stream().map(row -> {
			Map<String, Object> employee = new HashMap<>();
			employee.put("employee_cd", row[0]);
		    employee.put("employee_id", row[1]);
		    employee.put("employee_pw", row[2]);
		    employee.put("employee_dp", row[3]);
		    employee.put("employee_gd", row[4]);
		    employee.put("employee_hd", row[5]);
		    employee.put("employee_rd", row[6]);
		    employee.put("employee_rr", row[7]);
		    employee.put("employee_cg", row[8]);
		    employee.put("employee_nt", row[9]);
		    employee.put("employee_nm", row[10]);
		    employee.put("employee_bd", row[11]);
		    employee.put("employee_ad", row[12]);
		    employee.put("employee_sb", row[13]);
		    employee.put("employee_ph", row[14]);
		    employee.put("employee_em", row[15]);
		    employee.put("employee_pi", row[16]);
		    employee.put("employee_bs", row[17]);
		    employee.put("employee_bk", row[18]);
		    employee.put("employee_an", row[19]);
		    employee.put("employee_dt", row[20]);
		    employee.put("employee_wr", row[21]);
		    employee.put("employee_wd", row[22]);
		    employee.put("employee_mf", row[23]);
		    employee.put("employee_md", row[24]);
		    employee.put("employee_mg", row[25]);
		    employee.put("employee_rl", row[26]);
		    employee.put("employee_us", row[27]);
		    
			return employee;
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> select_EMPLOYEE_ALL() {
	    List<Object[]> result = EmployeeRepository.select_EMPLOYEE_ALL();

	    return result.stream().map(row -> {
	        Map<String, Object> employee = new HashMap<>();
	        employee.put("employee_cd", row[0]);
	        employee.put("employee_id", row[1]);
	        employee.put("employee_pw", row[2]);
	        employee.put("employee_dp", row[3]);
	        employee.put("employee_gd", row[4]);
	        employee.put("employee_hd", row[5]);
	        employee.put("employee_rd", row[6]);
	        employee.put("employee_rr", row[7]);
	        employee.put("employee_cg", row[8]);
	        employee.put("employee_nt", row[9]);
	        employee.put("employee_nm", row[10]);
	        employee.put("employee_bd", row[11]);
	        employee.put("employee_ad", row[12]);
	        employee.put("employee_sb", row[13]);
	        employee.put("employee_ph", row[14]);
	        employee.put("employee_em", row[15]);
	        employee.put("employee_pi", row[16]);
	        employee.put("employee_bs", row[17]);
	        employee.put("employee_bk", row[18]);
	        employee.put("employee_an", row[19]);
	        employee.put("employee_dt", row[20]);
	        employee.put("employee_wr", row[21]);
	        employee.put("employee_wd", row[22]);
	        employee.put("employee_mf", row[23]);
	        employee.put("employee_md", row[24]);
	        employee.put("employee_mg", row[25]);
	        employee.put("employee_rl", row[26]);
	        employee.put("employee_us", row[27]);

	        return employee;
	    }).collect(Collectors.toList());
	}

	
	public List<Map<String, Object>> select_EMPLOYEE_ALL_CD(String currentCd) {
		List<Object[]> result = EmployeeRepository.select_EMPLOYEE_ALL_CD(currentCd);
		
		return result.stream().map(row -> {
			Map<String, Object> employee = new HashMap<>();
			employee.put("employee_cd", row[0]);
			employee.put("employee_id", row[1]);
			employee.put("employee_pw", row[2]);
			employee.put("employee_dp", row[3]);
			employee.put("employee_gd", row[4]);
			employee.put("employee_hd", row[5]);
			employee.put("employee_rd", row[6]);
			employee.put("employee_rr", row[7]);
			employee.put("employee_cg", row[8]);
			employee.put("employee_nt", row[9]);
			employee.put("employee_nm", row[10]);
			employee.put("employee_bd", row[11]);
			employee.put("employee_ad", row[12]);
			employee.put("employee_sb", row[13]);
			employee.put("employee_ph", row[14]);
			employee.put("employee_em", row[15]);
			employee.put("employee_pi", row[16]);
			employee.put("employee_bs", row[17]);
			employee.put("employee_bk", row[18]);
			employee.put("employee_an", row[19]);
			employee.put("employee_dt", row[20]);
			employee.put("employee_wr", row[21]);
			employee.put("employee_wd", row[22]);
			employee.put("employee_mf", row[23]);
			employee.put("employee_md", row[24]);
			employee.put("employee_mg", row[25]);
			employee.put("employee_rl", row[26]);
			employee.put("employee_us", row[27]);
			
			return employee;
		}).collect(Collectors.toList());
	}

	
	
	public List<Map<String, Object>> select_FILTERED_EMPLOYEE(EmployeeFilterRequest filterRequest, String currentCd) {
		List<Object[]> result;
		
	    // currentCd가 null이 아니면 currentCd를 필터로 추가
	    if (currentCd != null) {
	        result = EmployeeRepository.select_FILTERED_EMPLOYEE_WITH_CD(filterRequest, currentCd);
	    } else {
	        result = EmployeeRepository.select_FILTERED_EMPLOYEE(filterRequest, currentCd);
	    }
		
		
		return result.stream().map(row -> {
			Map<String, Object> employee = new HashMap<>();
			employee.put("employee_cd", row[0]);
			employee.put("employee_id", row[1]);
			employee.put("employee_pw", row[2]);
			employee.put("employee_dp", row[3]);
			employee.put("employee_gd", row[4]);
			employee.put("employee_hd", row[5]);
			employee.put("employee_rd", row[6]);
			employee.put("employee_rr", row[7]);
			employee.put("employee_cg", row[8]);
			employee.put("employee_nt", row[9]);
			employee.put("employee_nm", row[10]);
			employee.put("employee_bd", row[11]);
			employee.put("employee_ad", row[12]);
			employee.put("employee_sb", row[13]);
			employee.put("employee_ph", row[14]);
			employee.put("employee_em", row[15]);
			employee.put("employee_pi", row[16]);
			employee.put("employee_bs", row[17]);
			employee.put("employee_bk", row[18]);
			employee.put("employee_an", row[19]);
			employee.put("employee_dt", row[20]);
			employee.put("employee_wr", row[21]);
			employee.put("employee_wd", row[22]);
			employee.put("employee_mf", row[23]);
			employee.put("employee_md", row[24]);
			employee.put("employee_mg", row[25]);
			employee.put("employee_rl", row[26]);
			employee.put("employee_us", row[27]);
			
			return employee;
		}).collect(Collectors.toList());
	}



    
    
    
    
	
}