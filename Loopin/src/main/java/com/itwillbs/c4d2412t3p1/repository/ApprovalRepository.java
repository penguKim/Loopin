package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Employee;

public interface ApprovalRepository extends JpaRepository<Approval, String>{
	
	@Query(value = "SELECT AP_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	
    // 인사코드 값으로 찾기
    @Query(value = "SELECT * FROM EMPLOYEE e WHERE e.employee_cd = :currentCd", nativeQuery = true)
    List<Approval> findByApprovalCd(@Param("currentCd") String currentCd);
	
}
