package com.itwillbs.c4d2412t3p1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, String> {


	@Query(value = "SELECT EM_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
}
