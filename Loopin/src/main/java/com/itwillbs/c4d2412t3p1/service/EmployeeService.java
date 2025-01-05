package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class EmployeeService {

	private final EmployeeRepository EmployeeRepository;

	@Autowired
	@Value("${file.upload-dir}")
	private String uploadDir;

	public List<Employee> findAll() {
		
		return EmployeeRepository.findAll();
	}


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


	public void delete_EMPLOYEE(List<Long> cds) {
		EmployeeRepository.deleteAllById(cds);
		
	}


	public void update_EMPLOYEE(EmployeeDTO employeeDTO, MultipartFile employee_pi) throws IOException {
	    Employee employee = EmployeeRepository.findById(employeeDTO.getEmployee_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다."));

	    if (employee_pi != null && !employee_pi.isEmpty()) {
	        String fileName = UUID.randomUUID().toString() + "_" + employee_pi.getOriginalFilename();
	        Path path = Paths.get("uploads/" + fileName);
	        Files.copy(employee_pi.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        employeeDTO.setEmployee_pi(fileName); // DTO에 새 파일 이름 반영
	    } else {
	        employeeDTO.setEmployee_pi(employee.getEmployee_pi()); // 기존 파일 이름 유지
	    }

	    employee.setEmployeeEntity(employee, employeeDTO);
	    EmployeeRepository.save(employee);
	}

	
	
	
}
