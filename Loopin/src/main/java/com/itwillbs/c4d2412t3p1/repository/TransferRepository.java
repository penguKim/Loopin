package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

	@Query("SELECT " + "t.transfer_id, " + "t.employee_cd, " + "e.employee_nm, " + "t.transfer_ad, "
			+ "transfer_type.common_nm AS transfer_ac, " + "from_position.common_nm AS transfer_og, "
			+ "to_position.common_nm AS transfer_ag, " + "from_department.common_nm AS transfer_od, "
			+ "to_department.common_nm AS transfer_adp " + "FROM TRANSFER t "
			+ "JOIN Employee e ON t.employee_cd = e.employee_cd "
			+ "LEFT JOIN COMMON_CODE transfer_type ON t.transfer_ac = transfer_type.common_cc AND transfer_type.common_gc = 'TRTYPE' "
			+ "LEFT JOIN COMMON_CODE from_position ON t.transfer_og = from_position.common_cc AND from_position.common_gc = 'POSITION' "
			+ "LEFT JOIN COMMON_CODE to_position ON t.transfer_ag = to_position.common_cc AND to_position.common_gc = 'POSITION' "
			+ "LEFT JOIN COMMON_CODE from_department ON t.transfer_od = from_department.common_cc AND from_department.common_gc = 'DEPARTMENT' "
			+ "LEFT JOIN COMMON_CODE to_department ON t.transfer_adp = to_department.common_cc AND to_department.common_gc = 'DEPARTMENT'")
	List<Object[]> findAllWithDetails();

	@Query("SELECT e.employee_cd, " + " e.employee_nm, " + "d.common_nm AS department_name, "
			+ "p.common_nm AS position_name " + "FROM Employee e "
			+ "LEFT JOIN COMMON_CODE d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' "
			+ "LEFT JOIN COMMON_CODE p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION'")
	List<Object[]> findEmployeeList();

	@Query("SELECT new map(e.employee_nm as employee_nm, d.common_nm as department_name) " + "FROM Employee e "
			+ "JOIN COMMON_CODE d ON e.employee_dp = d.common_cc "
			+ "WHERE e.employee_mg = true AND e.employee_dp = :transfer_adp AND d.common_gc = 'DEPARTMENT'")
	Map<String, Object> findDepartmentManager(@Param("transfer_adp") String transfer_adp);

}
