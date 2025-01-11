package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.c4d2412t3p1.entity.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, String>{

	@Query(value = "SELECT AP_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
}
