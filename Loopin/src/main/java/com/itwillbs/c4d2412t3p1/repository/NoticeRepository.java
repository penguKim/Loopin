package com.itwillbs.c4d2412t3p1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.itwillbs.c4d2412t3p1.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, String>{
	
	@Query(value = "SELECT NT_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
}
