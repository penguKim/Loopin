package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Employee;


public interface ApprovalRepository extends JpaRepository<Approval, String>{
	
	
	@Query(value = "SELECT AP_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	
    // 인사코드 값으로 찾기
    @Query(value = "SELECT * FROM APPROVAL a WHERE a.employee_cd = :currentCd", nativeQuery = true)
    List<Approval> findByApprovalCd(@Param("currentCd") String currentCd);
	
//     1차 결재권자 목록 조회
    @Query("SELECT e FROM Employee e " +
           "JOIN COMMON_CODE c ON e.employee_gd = c.common_cc " +
           "WHERE (:employee_gd <= '50' AND c.common_cc > :employee_gd AND c.common_cc < '90') " +
           "   OR (:employee_gd > '50' AND c.common_cc > :employee_gd)")
    List<Employee> findFirstApprovers(@Param("employee_gd") String employee_gd);

//     2차 결재권자 목록 조회
    @Query("SELECT e FROM Employee e " +
           "JOIN COMMON_CODE c ON e.employee_gd = c.common_cc " +
           "WHERE c.common_cc > (SELECT e2.employee_gd FROM Employee e2 WHERE e2.employee_cd = :approval_fa)")
    List<Employee> findSecondApprovers(@Param("approval_fa") String approval_fa);
    
    
    
    
    
    
//    @Query(value = "SELECT * FROM approval a " +
//            "WHERE (:#{#filterRequest.approvalCd} IS NULL OR a.approval_cd = :#{#filterRequest.approvalCd}) " +
//            "AND (:#{#filterRequest.approvalDv} IS NULL OR a.approval_dv = :#{#filterRequest.approvalDv}) " +
//            "AND (:#{#filterRequest.approvalWr} IS NULL OR a.approval_wr = :#{#filterRequest.approvalWr}) " +
//            "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
//            "     OR a.approval_sd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})",
//            nativeQuery = true)
//    List<Approval> select_FILTERED_APPROVAL(@Param("filterRequest") APPROVALFilterRequest filterRequest);

    
}
