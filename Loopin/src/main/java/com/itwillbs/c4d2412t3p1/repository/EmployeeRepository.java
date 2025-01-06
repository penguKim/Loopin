package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT employee_sb AS name, COUNT(*) AS data FROM EMPLOYEE GROUP BY employee_sb", nativeQuery = true)
    List<Map<String, Object>> findGenderStats();


}
