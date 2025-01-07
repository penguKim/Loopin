package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class EmployeeService {

	private final EmployeeRepository EmployeeRepository;

	private final CommonRepository commonRepository;

	@Autowired
	@Value("${file.upload-dir}")
	private String uploadDir;

	// 직원 조회
	public List<Employee> findAll() {
		
		return EmployeeRepository.findAll();
	}

	// 직원 등록
    public void insert_EMPLOYEE(EmployeeDTO employeeDTO, MultipartFile employee_pi) throws IOException {
        String fileName = null;

        // 파일 저장 처리
        if (employee_pi != null && !employee_pi.isEmpty()) {
            fileName = employee_pi.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/" + fileName);
        
             // 디렉토리 생성 (존재하지 않을 경우)
	        if (!Files.exists(uploadPath.getParent())) {
	            Files.createDirectories(uploadPath.getParent());
	        }
        
        // 파일 저장
        Files.copy(employee_pi.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        
        }
        // Employee 엔티티 생성
        Employee employee = Employee.createEmployee(employeeDTO, fileName);

        // 데이터베이스 저장
        EmployeeRepository.save(employee);
    }

    // 직원 삭제
	public void delete_EMPLOYEE(List<Long> cds) {
		EmployeeRepository.deleteAllById(cds);
		
	}

	// 직원 업데이트
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
	        Path path = Paths.get(uploadDir, fileName);

	        // 업로드 디렉터리 생성
	        if (!Files.exists(path.getParent())) {
	            Files.createDirectories(path.getParent());
	        }

	        // 파일 저장
	        Files.copy(employee_pi.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	        // 엔티티에 새 파일 이름 설정
	        employee.setEmployee_pi(fileName);
	    } else if (!"true".equals(employeeDTO.getPhotoDeleted())) {
	        // 사진 삭제 요청이 없을 경우, 기존 사진 유지
	        employeeDTO.setEmployee_pi(employee.getEmployee_pi());
	    }

	    // 엔티티 업데이트
	    employee.setEmployeeEntity(employee, employeeDTO);

	    // 데이터베이스 저장
	    EmployeeRepository.save(employee);
	}

	public void deleteEmployeePhoto(Long employee_cd) {
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
    public Employee findEmployeeById(Long employee_cd) {
        // Repository를 사용하여 데이터 조회
        return EmployeeRepository.findById(employee_cd)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
    }

    
    // 성별 차트 조회
    public List<Map<String, Object>> getEmployeeGenderStats() {
        return EmployeeRepository.findGenderStats();
    }
	
    
    // 모달 직급코드 가져오기
	public List<Common_code> selectGradeList(String string) {
		return commonRepository.selectGradeList("00", string);
	}
    
	
	
}
