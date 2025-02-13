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

    
//    @Query(value = "SELECT * FROM employee e " +
//            "WHERE (:#{#filterRequest.employeeCd} IS NULL OR e.employee_cd = :#{#filterRequest.employeeCd}) " +
//            "AND (:#{#filterRequest.employeeDp} IS NULL OR e.employee_dp = :#{#filterRequest.employeeDp}) " +
//            "AND (:#{#filterRequest.employeeGd} IS NULL OR e.employee_gd = :#{#filterRequest.employeeGd}) " +
//            "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
//            "     OR e.employee_hd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) " +
//            "AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%)",
//    nativeQuery = true)
//    List<Employee> select_FILTERED_EMPLOYEE(@Param("filterRequest") EmployeeFilterRequest filterRequest);

    @Query(value = "SELECT e.employee_cd AS employee_cd, \n"
            + "       e.employee_id AS employee_id, \n"
            + "       e.employee_pw AS employee_pw, \n"
            + "       d.common_nm AS employee_dp, \n"
            + "       p.common_nm AS employee_gd, \n"
            + "       e.employee_hd AS employee_hd, \n"
            + "       e.employee_rd AS employee_rd, \n"
            + "       e.employee_rr AS employee_rr, \n"
            + "       e.employee_cg AS employee_cg, \n"
            + "       e.employee_nt AS employee_nt, \n"
            + "       e.employee_nm AS employee_nm, \n"
            + "       e.employee_bd AS employee_bd, \n"
            + "       e.employee_ad AS employee_ad, \n"
            + "       e.employee_sb AS employee_sb, \n"
            + "       e.employee_ph AS employee_ph, \n"
            + "       e.employee_em AS employee_em, \n"
            + "       e.employee_pi AS employee_pi, \n"
            + "       e.employee_bs AS employee_bs, \n"
            + "       e.employee_bk AS employee_bk, \n"
            + "       e.employee_an AS employee_an, \n"
            + "       e.employee_dt AS employee_dt, \n"
            + "       e.employee_wr AS employee_wr, \n"
            + "       e.employee_wd AS employee_wd, \n"
            + "       e.employee_mf AS employee_mf, \n"
            + "       e.employee_md AS employee_md, \n"
            + "       q.common_nm AS employee_mg, \n"
            + "       m.common_nm AS employee_rl, \n"
            + "       a.common_nm AS employee_us \n"
            + "FROM employee e \n"
            + "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' \n"
            + "LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' \n"
            + "LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' \n"
            + "LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' \n"
            + "LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' \n"
            + "ORDER BY e.employee_cd ASC"
            , nativeQuery = true)
	List<Object[]> findAllWithDetails();

	
	
	
    @Query(value = "SELECT e.employee_cd AS employee_cd, \n"
            + "       e.employee_id AS employee_id, \n"
            + "       e.employee_pw AS employee_pw, \n"
            + "       d.common_nm AS employee_dp, \n"
            + "       p.common_nm AS employee_gd, \n"
            + "       e.employee_hd AS employee_hd, \n"
            + "       e.employee_rd AS employee_rd, \n"
            + "       e.employee_rr AS employee_rr, \n"
            + "       e.employee_cg AS employee_cg, \n"
            + "       e.employee_nt AS employee_nt, \n"
            + "       e.employee_nm AS employee_nm, \n"
            + "       e.employee_bd AS employee_bd, \n"
            + "       e.employee_ad AS employee_ad, \n"
            + "       e.employee_sb AS employee_sb, \n"
            + "       e.employee_ph AS employee_ph, \n"
            + "       e.employee_em AS employee_em, \n"
            + "       e.employee_pi AS employee_pi, \n"
            + "       e.employee_bs AS employee_bs, \n"
            + "       e.employee_bk AS employee_bk, \n"
            + "       e.employee_an AS employee_an, \n"
            + "       e.employee_dt AS employee_dt, \n"
            + "       e.employee_wr AS employee_wr, \n"
            + "       e.employee_wd AS employee_wd, \n"
            + "       e.employee_mf AS employee_mf, \n"
            + "       e.employee_md AS employee_md, \n"
            + "       q.common_nm AS employee_mg, \n"
            + "       m.common_nm AS employee_rl, \n"
            + "       a.common_nm AS employee_us \n"
            + "FROM employee e \n"
            + "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' \n"
            + "LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' \n"
            + "LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' \n"
            + "LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' \n"
            + "LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' \n"
			+ "WHERE e.employee_cd = :employeeCd"
			+ "ORDER BY e.employee_cd ASC"
			, nativeQuery = true)
	List<Object[]> findAllWithDetailsCd(@Param("employeeCd") String currentCd);

    
	
	@Query(value = "SELECT e.employee_cd AS employee_cd, \n"
	        + "       e.employee_id AS employee_id, \n"
	        + "       e.employee_pw AS employee_pw, \n"
	        + "       d.common_nm AS employee_dp, \n"
	        + "       p.common_nm AS employee_gd, \n"
	        + "       e.employee_hd AS employee_hd, \n"
	        + "       e.employee_rd AS employee_rd, \n"
	        + "       e.employee_rr AS employee_rr, \n"
	        + "       e.employee_cg AS employee_cg, \n"
	        + "       e.employee_nt AS employee_nt, \n"
	        + "       e.employee_nm AS employee_nm, \n"
	        + "       e.employee_bd AS employee_bd, \n"
	        + "       e.employee_ad AS employee_ad, \n"
	        + "       e.employee_sb AS employee_sb, \n"
	        + "       e.employee_ph AS employee_ph, \n"
	        + "       e.employee_em AS employee_em, \n"
	        + "       e.employee_pi AS employee_pi, \n"
	        + "       e.employee_bs AS employee_bs, \n"
	        + "       e.employee_bk AS employee_bk, \n"
	        + "       e.employee_an AS employee_an, \n"
	        + "       e.employee_dt AS employee_dt, \n"
	        + "       e.employee_wr AS employee_wr, \n"
	        + "       e.employee_wd AS employee_wd, \n"
	        + "       e.employee_mf AS employee_mf, \n"
	        + "       e.employee_md AS employee_md, \n"
	        + "       q.common_nm AS employee_mg, \n"
	        + "       m.common_nm AS employee_rl, \n"
	        + "       a.common_nm AS employee_us \n"
	        + "FROM employee e \n"
	        + "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' \n"
	        + "LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' \n"
	        + "LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' \n"
	        + "LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' \n"
	        + "LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION'"
	        + "ORDER BY e.employee_cd ASC"
	        ,nativeQuery = true)
	List<Object[]> select_EMPLOYEE_ALL();

	
	
	@Query(value = "SELECT e.employee_cd AS employee_cd, \n"
	        + "       e.employee_id AS employee_id, \n"
	        + "       e.employee_pw AS employee_pw, \n"
	        + "       d.common_nm AS employee_dp, \n"
	        + "       p.common_nm AS employee_gd, \n"
	        + "       e.employee_hd AS employee_hd, \n"
	        + "       e.employee_rd AS employee_rd, \n"
	        + "       e.employee_rr AS employee_rr, \n"
	        + "       e.employee_cg AS employee_cg, \n"
	        + "       e.employee_nt AS employee_nt, \n"
	        + "       e.employee_nm AS employee_nm, \n"
	        + "       e.employee_bd AS employee_bd, \n"
	        + "       e.employee_ad AS employee_ad, \n"
	        + "       e.employee_sb AS employee_sb, \n"
	        + "       e.employee_ph AS employee_ph, \n"
	        + "       e.employee_em AS employee_em, \n"
	        + "       e.employee_pi AS employee_pi, \n"
	        + "       e.employee_bs AS employee_bs, \n"
	        + "       e.employee_bk AS employee_bk, \n"
	        + "       e.employee_an AS employee_an, \n"
	        + "       e.employee_dt AS employee_dt, \n"
	        + "       e.employee_wr AS employee_wr, \n"
	        + "       e.employee_wd AS employee_wd, \n"
	        + "       e.employee_mf AS employee_mf, \n"
	        + "       e.employee_md AS employee_md, \n"
	        + "       q.common_nm AS employee_mg, \n"
	        + "       m.common_nm AS employee_rl, \n"
	        + "       a.common_nm AS employee_us \n"
	        + "FROM employee e \n"
	        + "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' \n"
	        + "LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' \n"
	        + "LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' \n"
	        + "LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' \n"
	        + "LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' \n"
	        + "WHERE (:currentCd IS NULL OR e.employee_cd = :currentCd)"
	        + "ORDER BY e.employee_cd ASC"
	        ,nativeQuery = true)
	List<Object[]> select_EMPLOYEE_ALL_CD(@Param("currentCd") String currentCd);
	
	
	
	@Query(value = "SELECT e.employee_cd AS employee_cd, \n"
	        + "       e.employee_id AS employee_id, \n"
	        + "       e.employee_pw AS employee_pw, \n"
	        + "       d.common_nm AS employee_dp, \n"
	        + "       p.common_nm AS employee_gd, \n"
	        + "       e.employee_hd AS employee_hd, \n"
	        + "       e.employee_rd AS employee_rd, \n"
	        + "       e.employee_rr AS employee_rr, \n"
	        + "       e.employee_cg AS employee_cg, \n"
	        + "       e.employee_nt AS employee_nt, \n"
	        + "       e.employee_nm AS employee_nm, \n"
	        + "       e.employee_bd AS employee_bd, \n"
	        + "       e.employee_ad AS employee_ad, \n"
	        + "       e.employee_sb AS employee_sb, \n"
	        + "       e.employee_ph AS employee_ph, \n"
	        + "       e.employee_em AS employee_em, \n"
	        + "       e.employee_pi AS employee_pi, \n"
	        + "       e.employee_bs AS employee_bs, \n"
	        + "       e.employee_bk AS employee_bk, \n"
	        + "       e.employee_an AS employee_an, \n"
	        + "       e.employee_dt AS employee_dt, \n"
	        + "       e.employee_wr AS employee_wr, \n"
	        + "       e.employee_wd AS employee_wd, \n"
	        + "       e.employee_mf AS employee_mf, \n"
	        + "       e.employee_md AS employee_md, \n"
	        + "       q.common_nm AS employee_mg, \n"
	        + "       m.common_nm AS employee_rl, \n"
	        + "       a.common_nm AS employee_us \n"
	        + "FROM employee e \n"
	        + "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' \n"
	        + "LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' \n"
	        + "LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' \n"
	        + "LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' \n"
	        + "LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' \n"
	        + "WHERE (:#{#filterRequest.employeeCd} IS NULL OR e.employee_cd LIKE %:#{#filterRequest.employeeCd}) \n"
	        + "  AND (:#{#filterRequest.employeeDp} IS NULL OR d.common_nm LIKE %:#{#filterRequest.employeeDp}%) \n"
	        + "  AND (:#{#filterRequest.employeeGd} IS NULL OR p.common_nm LIKE %:#{#filterRequest.employeeGd}%) \n"
	        + "  AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL \n"
	        + "       OR e.employee_hd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) \n"
	        + "  AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%) \n"
	        + "  AND (:currentCd IS NULL OR e.employee_cd = :currentCd)"
	        + "ORDER BY e.employee_cd ASC"
	        , nativeQuery = true)
	List<Object[]> select_FILTERED_EMPLOYEE(@Param("filterRequest") EmployeeFilterRequest filterRequest, 
	                                         @Param("currentCd") String currentCd);



	
	@Query(value = "SELECT e.employee_cd AS employee_cd, \n"
	        + "       e.employee_id AS employee_id, \n"
	        + "       e.employee_pw AS employee_pw, \n"
	        + "       d.common_nm AS employee_dp, \n"
	        + "       p.common_nm AS employee_gd, \n"
	        + "       e.employee_hd AS employee_hd, \n"
	        + "       e.employee_rd AS employee_rd, \n"
	        + "       e.employee_rr AS employee_rr, \n"
	        + "       e.employee_cg AS employee_cg, \n"
	        + "       e.employee_nt AS employee_nt, \n"
	        + "       e.employee_nm AS employee_nm, \n"
	        + "       e.employee_bd AS employee_bd, \n"
	        + "       e.employee_ad AS employee_ad, \n"
	        + "       e.employee_sb AS employee_sb, \n"
	        + "       e.employee_ph AS employee_ph, \n"
	        + "       e.employee_em AS employee_em, \n"
	        + "       e.employee_pi AS employee_pi, \n"
	        + "       e.employee_bs AS employee_bs, \n"
	        + "       e.employee_bk AS employee_bk, \n"
	        + "       e.employee_an AS employee_an, \n"
	        + "       e.employee_dt AS employee_dt, \n"
	        + "       e.employee_wr AS employee_wr, \n"
	        + "       e.employee_wd AS employee_wd, \n"
	        + "       e.employee_mf AS employee_mf, \n"
	        + "       e.employee_md AS employee_md, \n"
	        + "       q.common_nm AS employee_mg, \n"
	        + "       m.common_nm AS employee_rl, \n"
	        + "       a.common_nm AS employee_us \n"
	        + "FROM employee e \n"
	        + "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' \n"
	        + "LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' \n"
	        + "LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' \n"
	        + "LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' \n"
	        + "LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' \n"
	        + "WHERE (:#{#filterRequest.employeeCd} IS NULL OR e.employee_cd LIKE %:#{#filterRequest.employeeCd}) \n"
	        + "  AND (:#{#filterRequest.employeeDp} IS NULL OR d.common_nm LIKE %:#{#filterRequest.employeeDp}%) \n"
	        + "  AND (:#{#filterRequest.employeeGd} IS NULL OR p.common_nm LIKE %:#{#filterRequest.employeeGd}%) \n"
	        + "  AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL \n"
	        + "       OR e.employee_hd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) \n"
	        + "  AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%) \n"
	        + "  AND e.employee_cd = :currentCd"
	        + "ORDER BY e.employee_cd ASC"
	        ,nativeQuery = true)
	List<Object[]> select_FILTERED_EMPLOYEE_WITH_CD(@Param("filterRequest") EmployeeFilterRequest filterRequest, 
	                                                 @Param("currentCd") String currentCd);

	
	
	
}