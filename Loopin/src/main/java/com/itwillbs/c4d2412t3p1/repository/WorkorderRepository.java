package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.c4d2412t3p1.entity.Workorder;

public interface WorkorderRepository extends JpaRepository<Workorder, String> {
	
	@Query(value = "SELECT WORKORDER_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
}
