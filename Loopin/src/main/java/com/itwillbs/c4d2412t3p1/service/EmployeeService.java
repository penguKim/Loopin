package com.itwillbs.c4d2412t3p1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class EmployeeService {

	private final EmployeeRepository EmployeeRepository;

	public List<Employee> findAll() {
		
		return EmployeeRepository.findAll();
	}

	public void save(EmployeeDTO employeeDTO) {

		Employee employee = Employee.createEmployee(employeeDTO);
		
		EmployeeRepository.save(employee);
	}
	
	
	
}
