package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.LogFilterRequest.APPROVALFilterRequest;

public interface ApprovalRepository extends JpaRepository<Approval, String>{
	
	@Query(value = "SELECT AP_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	
    // 인사코드 값으로 찾기
    @Query(value = "SELECT * FROM APPROVAL a WHERE a.employee_cd = :currentCd", nativeQuery = true)
    List<Approval> findByApprovalCd(@Param("currentCd") String currentCd);
	
    
    @Query(value = "SELECT * FROM approval a " +
            "WHERE (:#{#filterRequest.approvalCd} IS NULL OR a.approval_cd = :#{#filterRequest.approvalCd}) " +
            "AND (:#{#filterRequest.approvalDv} IS NULL OR a.approval_dv = :#{#filterRequest.approvalDv}) " +
            "AND (:#{#filterRequest.approvalWr} IS NULL OR a.approval_wr = :#{#filterRequest.approvalWr}) " +
            "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
            "     OR a.approval_sd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})",
            nativeQuery = true)
    List<Approval> select_FILTERED_APPROVAL(@Param("filterRequest") APPROVALFilterRequest filterRequest);

    
}
