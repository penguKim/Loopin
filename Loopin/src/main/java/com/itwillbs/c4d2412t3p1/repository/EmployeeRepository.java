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
import com.itwillbs.c4d2412t3p1.util.FilterRequest.LogFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EmployeeFilterRequest;

import jakarta.transaction.Transactional;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, String> {

    @Query("SELECT e FROM Employee e WHERE e.employee_id = :employee_id")
    Optional<Employee> findByEmployee_id(@Param("employee_id") String employee_id);

    // 인사카드 시퀀스 조회
	@Query(value = "SELECT EM_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
	// 기간별 남녀성비 조회
	@Query(value = "SELECT employee_sb AS name, COUNT(*) AS data " +
            "FROM employee " +
            "WHERE employee_hd BETWEEN :startDt AND :endDt " +
            "AND (employee_rd IS NULL OR employee_rd >= :startDt) " +
            "GROUP BY employee_sb", nativeQuery = true)
	List<Map<String, Object>> findEmployeeGenderStatsByDate(
	  @Param("startDt") String startDt, 
	  @Param("endDt") String endDt);

	// 부서별 인원 조회
	@Query(value = "SELECT COUNT(*) AS data, " +
            "COALESCE(pos.COMMON_NM, '직급 없음') AS name " +
            "FROM employee e1_0 " +
            "LEFT JOIN (SELECT COMMON_CC, COMMON_NM " +
                      "FROM COMMON_CODE " +
                      "WHERE COMMON_GC = 'DEPARTMENT') pos " +
            "ON e1_0.employee_dp = pos.COMMON_CC " +
            "WHERE e1_0.employee_hd BETWEEN :startDt AND :endDt " +
            "AND (e1_0.employee_rd IS NULL OR e1_0.employee_rd >= :startDt) " +
            "GROUP BY e1_0.employee_dp, pos.COMMON_NM", nativeQuery = true)
	List<Map<String, Object>> getEmployeeDeptStatsByDate(
			@Param("startDt") String startDt, 
			@Param("endDt") String endDt);

	// 직위별 인원 조회
	@Query(value = "SELECT COUNT(*) AS data, " +
            "COALESCE(pos.COMMON_NM, '직급 없음') AS name " +
            "FROM employee e1_0 " +
            "LEFT JOIN (SELECT COMMON_CC, COMMON_NM " +
                      "FROM COMMON_CODE " +
                      "WHERE COMMON_GC = 'POSITION') pos " +
            "ON e1_0.employee_gd = pos.COMMON_CC " +
            "WHERE e1_0.employee_hd BETWEEN :startDt AND :endDt " +
            "AND (e1_0.employee_rd IS NULL OR e1_0.employee_rd >= :startDt) " +
            "GROUP BY e1_0.employee_gd, pos.COMMON_NM", nativeQuery = true)
List<Map<String, Object>> getEmployeePosiStatsByDate(
        @Param("startDt") String startDt, 
        @Param("endDt") String endDt);

	
	
    // 월별 입사자 수 조회
	@Query(value = "SELECT COUNT(e1_0.employee_cd) " +
            "FROM employee e1_0 " +
            "WHERE TO_DATE(e1_0.employee_hd, 'YYYY-MM-DD') BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD') " +
            "GROUP BY TO_CHAR(TO_DATE(e1_0.employee_hd, 'YYYY-MM-DD'), 'YYYY-MM') " +
            "ORDER BY TO_CHAR(TO_DATE(e1_0.employee_hd, 'YYYY-MM-DD'), 'YYYY-MM')",
    nativeQuery = true)
    List<Integer> findHireCountsByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 월별 퇴사자 수 조회
	@Query(value = "SELECT COUNT(e1_0.employee_rd) " +
            "FROM employee e1_0 " +
            "WHERE TO_DATE(e1_0.employee_rd, 'YYYY-MM-DD') BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD') " +
            "GROUP BY TO_CHAR(TO_DATE(e1_0.employee_rd, 'YYYY-MM-DD'), 'YYYY-MM') " +
            "ORDER BY TO_CHAR(TO_DATE(e1_0.employee_rd, 'YYYY-MM-DD'), 'YYYY-MM')",
    nativeQuery = true)
    List<Integer> findRetireCountsByMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = 
            "SELECT DISTINCT TO_CHAR(TO_DATE(e.employee_hd, 'YYYY-MM-DD'), 'YYYY-MM') " +
            "FROM employee e " +
            "WHERE TO_DATE(e.employee_hd, 'YYYY-MM-DD') BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD') " +
            "UNION " +
            "SELECT DISTINCT TO_CHAR(TO_DATE(e.employee_rd, 'YYYY-MM-DD'), 'YYYY-MM') " +
            "FROM employee e " +
            "WHERE TO_DATE(e.employee_rd, 'YYYY-MM-DD') BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD') " +
            "ORDER BY 1",
            nativeQuery = true)
    List<String> findDistinctMonths(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 인사코드 값으로 찾기
    @Query(value = "SELECT * FROM EMPLOYEE e WHERE e.employee_cd = :currentCd", nativeQuery = true)
    List<Employee> findByEmployeeCd(@Param("currentCd") String currentCd);


    // 특정 아이디 존재 여부 확인 메서드
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM employee " +
            "WHERE employee_id = :employee_id", 
    nativeQuery = true)
    int existsByEmployeeId(@Param("employee_id") String employee_id);
    
    
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE employee SET employee_us = :status WHERE employee_cd IN (:employeeCds)", nativeQuery = true)
    void updateEmployeeStatus(@Param("employeeCds") List<String> employeeCds, @Param("status") Boolean status);

    
    @Query(value = "SELECT * FROM employee e " +
            "WHERE (:#{#filterRequest.employeeCd} IS NULL OR e.employee_cd = :#{#filterRequest.employeeCd}) " +
            "AND (:#{#filterRequest.employeeDp} IS NULL OR e.employee_dp = :#{#filterRequest.employeeDp}) " +
            "AND (:#{#filterRequest.employeeGd} IS NULL OR e.employee_gd = :#{#filterRequest.employeeGd}) " +
            "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
            "     OR e.employee_hd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) " +
            "AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%)",
    nativeQuery = true)
    List<Employee> select_FILTERED_EMPLOYEE(@Param("filterRequest") EmployeeFilterRequest filterRequest);

    
	
}