package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, String> {

    @Query("SELECT e FROM Employee e WHERE e.employee_id = :employee_id")
    Optional<Employee> findByEmployee_id(@Param("employee_id") String employee_id);

    @Query(value = "SELECT employee_sb AS name, COUNT(*) AS data FROM EMPLOYEE GROUP BY employee_sb", nativeQuery = true)
    List<Map<String, Object>> findGenderStats();


	@Query(value = "SELECT EM_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
}
