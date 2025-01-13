package com.itwillbs.c4d2412t3p1.config;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

	private final EmployeeRepository employeeRepository;
	
	@Override
	public UserDetails loadUserByUsername(String employee_id) throws UsernameNotFoundException {
		
		Employee employee = employeeRepository.findByEmployee_id(employee_id).orElseThrow(() -> 
			new UsernameNotFoundException("없는 회원"));
		
		System.out.println(employee.toString());
		
		return new EmployeeDetails(employee);
	}
}
