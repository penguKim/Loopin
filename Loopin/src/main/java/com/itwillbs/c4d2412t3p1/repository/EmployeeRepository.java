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
	@Query(value = """
		    SELECT 
		        employee_sb AS name, 
		        COUNT(*) AS data
		    FROM employee
		    GROUP BY employee_sb
		    """, nativeQuery = true)
	List<Map<String, Object>> findEmployeeGenderStatsByDate();

	// 부서별 인원 조회
	@Query(value = """
		    SELECT 
		        COUNT(*) AS data, 
		        COALESCE(pos.COMMON_NM, '직급 없음') AS name
		    FROM employee e1_0
		    LEFT JOIN (
		        SELECT COMMON_CC, COMMON_NM
		        FROM COMMON_CODE
		        WHERE COMMON_GC = 'DEPARTMENT'
		    ) pos ON e1_0.employee_dp = pos.COMMON_CC
		    GROUP BY e1_0.employee_dp, pos.COMMON_NM
	    """, nativeQuery = true)
	List<Map<String, Object>> getEmployeeDeptStatsByDate();

	// 직위별 인원 조회
	@Query(value = """
		    SELECT 
		        COUNT(*) AS data, 
		        COALESCE(pos.COMMON_NM, '직급 없음') AS name
		    FROM employee e1_0
		    LEFT JOIN (
		        SELECT COMMON_CC, COMMON_NM
		        FROM COMMON_CODE
		        WHERE COMMON_GC = 'POSITION'
		    ) pos ON e1_0.employee_gd = pos.COMMON_CC
		    GROUP BY e1_0.employee_gd, pos.COMMON_NM
	    """, nativeQuery = true)
List<Map<String, Object>> getEmployeePosiStatsByDate();

	
	
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

    
    @Query(value = """
		    SELECT e.employee_cd AS employee_cd
			       , e.employee_id AS employee_id
			       , e.employee_pw AS employee_pw
			       , d.common_nm AS employee_dp
			       , p.common_nm AS employee_gd
			       , e.employee_hd AS employee_hd
			       , e.employee_rd AS employee_rd
			       , e.employee_rr AS employee_rr
			       , e.employee_cg AS employee_cg
			       , e.employee_nt AS employee_nt
			       , e.employee_nm AS employee_nm
			       , e.employee_bd AS employee_bd
			       , e.employee_ad AS employee_ad
			       , e.employee_sb AS employee_sb
			       , e.employee_ph AS employee_ph
			       , e.employee_em AS employee_em
			       , e.employee_pi AS employee_pi
			       , e.employee_bs AS employee_bs
			       , e.employee_bk AS employee_bk
			       , e.employee_an AS employee_an
			       , e.employee_dt AS employee_dt
			       , e.employee_wr AS employee_wr
			       , e.employee_wd AS employee_wd
			       , e.employee_mf AS employee_mf
			       , e.employee_md AS employee_md
			       , q.common_nm AS employee_mg
			       , m.common_nm AS employee_rl
			       , a.common_nm AS employee_us 
    	    FROM employee e
    	    LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT'
    	    LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION'
    	    LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN'
    	    LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE'
    	    LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION'
    	""", nativeQuery = true)
    List<Object[]> findAllWithDetails();

	
	
    @Query(value = """
		    SELECT e.employee_cd AS employee_cd
			       , e.employee_id AS employee_id
			       , e.employee_pw AS employee_pw
			       , d.common_nm AS employee_dp
			       , p.common_nm AS employee_gd
			       , e.employee_hd AS employee_hd
			       , e.employee_rd AS employee_rd
			       , e.employee_rr AS employee_rr
			       , e.employee_cg AS employee_cg
			       , e.employee_nt AS employee_nt
			       , e.employee_nm AS employee_nm
			       , e.employee_bd AS employee_bd
			       , e.employee_ad AS employee_ad
			       , e.employee_sb AS employee_sb
			       , e.employee_ph AS employee_ph
			       , e.employee_em AS employee_em
			       , e.employee_pi AS employee_pi
			       , e.employee_bs AS employee_bs
			       , e.employee_bk AS employee_bk
			       , e.employee_an AS employee_an
			       , e.employee_dt AS employee_dt
			       , e.employee_wr AS employee_wr
			       , e.employee_wd AS employee_wd
			       , e.employee_mf AS employee_mf
			       , e.employee_md AS employee_md
			       , q.common_nm AS employee_mg
			       , m.common_nm AS employee_rl
			       , a.common_nm AS employee_us 
    	    FROM employee e
    	    LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT'
    	    LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION'
    	    LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN'
    	    LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE'
    	    LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION'
    		WHERE e.employee_cd = :currentCd
    	""", nativeQuery = true)
	List<Object[]> findAllWithDetailsCd(@Param("currentCd") String currentCd);

    
	
    @Query(value = """
		    SELECT e.employee_cd AS employee_cd
			       , e.employee_id AS employee_id
			       , e.employee_pw AS employee_pw
			       , d.common_nm AS employee_dp
			       , p.common_nm AS employee_gd
			       , e.employee_hd AS employee_hd
			       , e.employee_rd AS employee_rd
			       , e.employee_rr AS employee_rr
			       , e.employee_cg AS employee_cg
			       , e.employee_nt AS employee_nt
			       , e.employee_nm AS employee_nm
			       , e.employee_bd AS employee_bd
			       , e.employee_ad AS employee_ad
			       , e.employee_sb AS employee_sb
			       , e.employee_ph AS employee_ph
			       , e.employee_em AS employee_em
			       , e.employee_pi AS employee_pi
			       , e.employee_bs AS employee_bs
			       , e.employee_bk AS employee_bk
			       , e.employee_an AS employee_an
			       , e.employee_dt AS employee_dt
			       , e.employee_wr AS employee_wr
			       , e.employee_wd AS employee_wd
			       , e.employee_mf AS employee_mf
			       , e.employee_md AS employee_md
			       , q.common_nm AS employee_mg
			       , m.common_nm AS employee_rl
			       , a.common_nm AS employee_us 
    	    FROM employee e
    	    LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT'
    	    LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION'
    	    LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN'
    	    LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE'
    	    LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION'
    	""", nativeQuery = true)
	List<Object[]> select_EMPLOYEE_ALL();

	
	
    @Query(value = """
		    SELECT e.employee_cd AS employee_cd
			       , e.employee_id AS employee_id
			       , e.employee_pw AS employee_pw
			       , d.common_nm AS employee_dp
			       , p.common_nm AS employee_gd
			       , e.employee_hd AS employee_hd
			       , e.employee_rd AS employee_rd
			       , e.employee_rr AS employee_rr
			       , e.employee_cg AS employee_cg
			       , e.employee_nt AS employee_nt
			       , e.employee_nm AS employee_nm
			       , e.employee_bd AS employee_bd
			       , e.employee_ad AS employee_ad
			       , e.employee_sb AS employee_sb
			       , e.employee_ph AS employee_ph
			       , e.employee_em AS employee_em
			       , e.employee_pi AS employee_pi
			       , e.employee_bs AS employee_bs
			       , e.employee_bk AS employee_bk
			       , e.employee_an AS employee_an
			       , e.employee_dt AS employee_dt
			       , e.employee_wr AS employee_wr
			       , e.employee_wd AS employee_wd
			       , e.employee_mf AS employee_mf
			       , e.employee_md AS employee_md
			       , q.common_nm AS employee_mg
			       , m.common_nm AS employee_rl
			       , a.common_nm AS employee_us 
    	    FROM employee e
    	    LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT'
    	    LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION'
    	    LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN'
    	    LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE'
    	    LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION'
    		WHERE (:currentCd IS NULL OR e.employee_cd = :currentCd)
    	""", nativeQuery = true)
	List<Object[]> select_EMPLOYEE_ALL_CD(@Param("currentCd") String currentCd);
	
	
	@Query(value = """
		    SELECT e.employee_cd AS employee_cd
			       , e.employee_id AS employee_id
			       , e.employee_pw AS employee_pw
			       , d.common_nm AS employee_dp
			       , p.common_nm AS employee_gd
			       , e.employee_hd AS employee_hd
			       , e.employee_rd AS employee_rd
			       , e.employee_rr AS employee_rr
			       , e.employee_cg AS employee_cg
			       , e.employee_nt AS employee_nt
			       , e.employee_nm AS employee_nm
			       , e.employee_bd AS employee_bd
			       , e.employee_ad AS employee_ad
			       , e.employee_sb AS employee_sb
			       , e.employee_ph AS employee_ph
			       , e.employee_em AS employee_em
			       , e.employee_pi AS employee_pi
			       , e.employee_bs AS employee_bs
			       , e.employee_bk AS employee_bk
			       , e.employee_an AS employee_an
			       , e.employee_dt AS employee_dt
			       , e.employee_wr AS employee_wr
			       , e.employee_wd AS employee_wd
			       , e.employee_mf AS employee_mf
			       , e.employee_md AS employee_md
			       , q.common_nm AS employee_mg
			       , m.common_nm AS employee_rl
			       , a.common_nm AS employee_us 
		    FROM employee e 
		    LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' 
		    LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' 
		    LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' 
		    LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' 
		    LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' 
		    WHERE (:#{#filterRequest.employeeCd} IS NULL OR e.employee_cd LIKE %:#{#filterRequest.employeeCd}) 
		      AND (:#{#filterRequest.employeeDp} IS NULL OR d.common_nm LIKE %:#{#filterRequest.employeeDp}%) 
		      AND (:#{#filterRequest.employeeGd} IS NULL OR p.common_nm LIKE %:#{#filterRequest.employeeGd}%) 
		      AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL 
		           OR e.employee_hd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) 
		      AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%) 
		      AND (:currentCd IS NULL OR e.employee_cd = :currentCd)
		""", nativeQuery = true)
	List<Object[]> select_FILTERED_EMPLOYEE(@Param("filterRequest") EmployeeFilterRequest filterRequest, 
											@Param("currentCd") String currentCd);



	
	@Query(value = """
		    SELECT e.employee_cd AS employee_cd
			       , e.employee_id AS employee_id
			       , e.employee_pw AS employee_pw
			       , d.common_nm AS employee_dp
			       , p.common_nm AS employee_gd
			       , e.employee_hd AS employee_hd
			       , e.employee_rd AS employee_rd
			       , e.employee_rr AS employee_rr
			       , e.employee_cg AS employee_cg
			       , e.employee_nt AS employee_nt
			       , e.employee_nm AS employee_nm
			       , e.employee_bd AS employee_bd
			       , e.employee_ad AS employee_ad
			       , e.employee_sb AS employee_sb
			       , e.employee_ph AS employee_ph
			       , e.employee_em AS employee_em
			       , e.employee_pi AS employee_pi
			       , e.employee_bs AS employee_bs
			       , e.employee_bk AS employee_bk
			       , e.employee_an AS employee_an
			       , e.employee_dt AS employee_dt
			       , e.employee_wr AS employee_wr
			       , e.employee_wd AS employee_wd
			       , e.employee_mf AS employee_mf
			       , e.employee_md AS employee_md
			       , q.common_nm AS employee_mg
			       , m.common_nm AS employee_rl
			       , a.common_nm AS employee_us 
		    FROM employee e 
		    LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' 
		    LEFT JOIN common_code p ON e.employee_gd = p.common_cc AND p.common_gc = 'POSITION' 
		    LEFT JOIN common_code a ON e.employee_us = a.common_cc AND a.common_gc = 'USEYN' 
		    LEFT JOIN common_code q ON e.employee_mg = q.common_cc AND q.common_gc = 'DPTYPE' 
		    LEFT JOIN common_code m ON e.employee_rl = m.common_cc AND m.common_gc = 'PERMISSION' 
		    WHERE (:#{#filterRequest.employeeCd} IS NULL OR e.employee_cd LIKE %:#{#filterRequest.employeeCd}) 
		      AND (:#{#filterRequest.employeeDp} IS NULL OR d.common_nm LIKE %:#{#filterRequest.employeeDp}%) 
		      AND (:#{#filterRequest.employeeGd} IS NULL OR p.common_nm LIKE %:#{#filterRequest.employeeGd}%) 
		      AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL 
		           OR e.employee_hd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) 
		      AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%) 
		      AND e.employee_cd = :currentCd
		""", nativeQuery = true)
	List<Object[]> select_FILTERED_EMPLOYEE_WITH_CD(@Param("filterRequest") EmployeeFilterRequest filterRequest, 
	                                                 @Param("currentCd") String currentCd);

	// 2025-02-14 김기렬(생산계획 등록 시 담당자리스트를 가져오기 위함)
	// 부서(employee_dp) 기준 조회 (예: '60'인 전체 사원)
	@Query(value = "SELECT * FROM employee WHERE employee_dp = :employee_dp", nativeQuery = true)
    List<Employee> select_EMPLOYEE_BY_DP(@Param("employee_dp") String employee_dp);
	
	// 사원번호(employee_cd)와 부서(employee_dp) 모두 조건에 해당하는 사원 조회
    @Query(value = "SELECT * FROM employee WHERE employee_cd = :employee_cd AND employee_dp = :employee_dp", nativeQuery = true)
    List<Employee> select_EMPLOYEE_BY_CD_DP(@Param("employee_cd") String employee_cd,
                                              @Param("employee_dp") String employee_dp);
}