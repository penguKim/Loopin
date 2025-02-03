package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Employee;

public interface ApprovalRepository extends JpaRepository<Approval, String> {

	@Query(value = "SELECT AP_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	// 인사코드 값으로 찾기
	@Query("SELECT a FROM Approval a WHERE a.approval_wr = :currentId")
	List<Approval> findByApprovalCd(@Param("currentId") String currentId);
	
//     1차 결재권자 목록 조회
	@Query("SELECT e FROM Employee e " + "JOIN COMMON_CODE c ON e.employee_gd = c.common_cc "
			+ "WHERE c.common_gc = 'DEPARTMENT' " + // 직위 관련 필터 추가
			"AND ((TO_NUMBER(:employee_gd) <= 50 AND TO_NUMBER(c.common_cc) > TO_NUMBER(:employee_gd) AND TO_NUMBER(c.common_cc) < 90) "
			+ "   OR (TO_NUMBER(:employee_gd) > 50 AND TO_NUMBER(c.common_cc) > TO_NUMBER(:employee_gd))) "
			+ "ORDER BY TO_NUMBER(e.employee_gd) ASC")
	List<Employee> findFirstApprovers(@Param("employee_gd") String employee_gd);

//     2차 결재권자 목록 조회
	@Query("SELECT e FROM Employee e " + "JOIN COMMON_CODE c ON e.employee_gd = c.common_cc "
			+ "WHERE c.common_gc = 'POSITION' " + // 직위 관련 필터 추가
			"AND TO_NUMBER(c.common_cc) > (SELECT TO_NUMBER(e2.employee_gd) FROM Employee e2 WHERE e2.employee_cd = :approval_fa) "
			+ "ORDER BY TO_NUMBER(e.employee_gd) ASC")
	List<Employee> findSecondApprovers(@Param("approval_fa") String approval_fa);

//	오늘이 결재 시작일인 목록 조회
	@Query("SELECT a FROM Approval a WHERE a.approval_sd = :today AND a.approval_av = '10'")
	List<Approval> findByApprovalSd(@Param("today") String today);

//	작성자, 결재권자 기준 조회
	@Query("""
			    SELECT a FROM Approval a
			    WHERE a.approval_fa = :currentCd
			       OR a.approval_sa = :currentCd
			""")
	List<Approval> findByApprover(@Param("currentCd") String currentCd);

	@Query("SELECT e FROM Employee e WHERE e.employee_cd = :employeeCd")
	Optional<Employee> findByEmployeeCd(@Param("employeeCd") String employeeCd);

//    @Query(value = "SELECT * FROM approval a " +
//            "WHERE (:#{#filterRequest.approvalCd} IS NULL OR a.approval_cd = :#{#filterRequest.approvalCd}) " +
//            "AND (:#{#filterRequest.approvalDv} IS NULL OR a.approval_dv = :#{#filterRequest.approvalDv}) " +
//            "AND (:#{#filterRequest.approvalWr} IS NULL OR a.approval_wr = :#{#filterRequest.approvalWr}) " +
//            "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
//            "     OR a.approval_sd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})",
//            nativeQuery = true)
//    List<Approval> select_FILTERED_APPROVAL(@Param("filterRequest") APPROVALFilterRequest filterRequest);

}
