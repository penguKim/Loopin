package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

	@Query("SELECT " + "t.transfer_id, " + "t.employee_cd, " + "e.employee_nm, " + "t.transfer_ad, "
			+ "transfer_type.common_nm AS transfer_ac, " + "from_position.common_nm AS transfer_og, "
			+ "to_position.common_nm AS transfer_ag, " + "from_department.common_nm AS transfer_od, "
			+ "to_department.common_nm AS transfer_adp, "
			+ "CASE WHEN t.transfer_aw = true THEN '완료' ELSE '대기' END AS transfer_aw, " + "t.transfer_mg "
			+ "FROM TRANSFER t " + "JOIN Employee e ON t.employee_cd = e.employee_cd "
			+ "LEFT JOIN COMMON_CODE transfer_type ON t.transfer_ac = transfer_type.common_cc AND transfer_type.common_gc = 'TRTYPE' "
			+ "LEFT JOIN COMMON_CODE from_position ON t.transfer_og = from_position.common_cc AND from_position.common_gc = 'POSITION' "
			+ "LEFT JOIN COMMON_CODE to_position ON t.transfer_ag = to_position.common_cc AND to_position.common_gc = 'POSITION' "
			+ "LEFT JOIN COMMON_CODE from_department ON t.transfer_od = from_department.common_cc AND from_department.common_gc = 'DEPARTMENT' "
			+ "LEFT JOIN COMMON_CODE to_department ON t.transfer_adp = to_department.common_cc AND to_department.common_gc = 'DEPARTMENT'")
	List<Object[]> findAllWithDetails();

	
	// 특정 employee_cd 데이터 조회
	@Query("SELECT " + "t.transfer_id, " + "t.employee_cd, " + "e.employee_nm, " + "t.transfer_ad, "
			+ "transfer_type.common_nm AS transfer_ac, " + "from_position.common_nm AS transfer_og, "
			+ "to_position.common_nm AS transfer_ag, " + "from_department.common_nm AS transfer_od, "
			+ "to_department.common_nm AS transfer_adp, "
			+ "CASE WHEN t.transfer_aw = true THEN '완료' ELSE '대기' END AS transfer_aw " + "FROM TRANSFER t "
			+ "JOIN Employee e ON t.employee_cd = e.employee_cd "
			+ "LEFT JOIN COMMON_CODE transfer_type ON t.transfer_ac = transfer_type.common_cc AND transfer_type.common_gc = 'TRTYPE' "
			+ "LEFT JOIN COMMON_CODE from_position ON t.transfer_og = from_position.common_cc AND from_position.common_gc = 'POSITION' "
			+ "LEFT JOIN COMMON_CODE to_position ON t.transfer_ag = to_position.common_cc AND to_position.common_gc = 'POSITION' "
			+ "LEFT JOIN COMMON_CODE from_department ON t.transfer_od = from_department.common_cc AND from_department.common_gc = 'DEPARTMENT' "
			+ "LEFT JOIN COMMON_CODE to_department ON t.transfer_adp = to_department.common_cc AND to_department.common_gc = 'DEPARTMENT' "
			+ "WHERE t.employee_cd = :employee_cd")
	List<Object[]> findAllWithDetailsByEmployeeCd(@Param("employee_cd") String employee_cd);

	@Query("SELECT e.employee_cd, " + " e.employee_nm, " + "d.common_nm AS department_name, "
			+ "p.common_nm AS position_name " + "FROM Employee e "
			+ "LEFT JOIN COMMON_CODE d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' "
			+ "LEFT JOIN COMMON_CODE p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION'")
	List<Object[]> findEmployeeList();

	@Query("SELECT new map(e.employee_cd as employee_cd, e.employee_nm as employee_nm, d.common_nm as department_name) "
			+ "FROM Employee e " + "JOIN COMMON_CODE d ON e.employee_dp = d.common_cc "
			+ "WHERE e.employee_mg = true AND e.employee_dp = :transfer_adp AND d.common_gc = 'DEPARTMENT'")
	Map<String, Object> findDepartmentManager(@Param("transfer_adp") String transfer_adp);

	@Modifying
	@Query(value = """
			    UPDATE EMPLOYEE
			    SET employee_dp = :transfer_adp,
			        employee_gd = :transfer_ag,
			        employee_mg = :transfer_mg
			    WHERE employee_cd = :employee_cd
			""", nativeQuery = true)
	void updateEmployeeDepartmentAndGrade(@Param("employee_cd") String employee_cd,
			@Param("transfer_adp") String transfer_adp, @Param("transfer_ag") String transfer_ag,
			@Param("transfer_mg") Boolean transfer_mg);

	@Modifying
	@Query(value = """
			    UPDATE EMPLOYEE
			    SET employee_mg = :employee_mg
			    WHERE employee_cd = :employee_cd
			""", nativeQuery = true)
	void updateEmployeeManager(@Param("employee_cd") String employee_cd, @Param("employee_mg") Boolean employee_mg);

	// 인사발령날짜가 오늘날짜 인경우
	@Modifying
	@Query(value = """
			    UPDATE TRANSFER
			    SET transfer_aw = 1
			    WHERE transfer_id = :transfer_id
			""", nativeQuery = true)
	void updateTransfer_aw(@Param("transfer_id") Long transfer_id);

	
	
	@Query(value = """
			    SELECT *
			    FROM TRANSFER
			    WHERE transfer_ad = :today AND transfer_aw = 0
			""", nativeQuery = true)
	List<Transfer> findTransfersByDate(@Param("today") String today);

	@Query("SELECT new map(t.transfer_id as transfer_id, t.employee_cd as employee_cd, "
			+ "t.transfer_ac as transfer_ac, t.transfer_ad as transfer_ad, t.transfer_adp as transfer_adp, "
			+ "t.transfer_ag as transfer_ag, t.transfer_aw as transfer_aw, t.transfer_od as transfer_od, "
			+ "t.transfer_og as transfer_og, t.transfer_mg as transfer_mg) "
			+ "FROM TRANSFER t WHERE t.transfer_id = :transfer_id")
	Map<String, Object> selectTransferById(@Param("transfer_id") Long transferId);

}
