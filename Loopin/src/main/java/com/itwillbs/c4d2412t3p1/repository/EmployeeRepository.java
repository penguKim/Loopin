package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.c4d2412t3p1.entity.Employee;

public interface EmployeeRepository  extends JpaRepository<Employee, String> {

}
