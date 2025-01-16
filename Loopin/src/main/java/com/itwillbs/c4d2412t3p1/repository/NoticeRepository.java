package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Notice;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.NoticeFilterRequest;

public interface NoticeRepository extends JpaRepository<Notice, String>{
	
	@Query(value = "SELECT NT_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	
	@Query(value = "SELECT * FROM notice n " +
	        "WHERE (:#{#filterRequest.noticeWr} IS NULL OR n.notice_wr LIKE %:#{#filterRequest.noticeWr}%) " +
	        "AND (:#{#filterRequest.noticeTt} IS NULL OR n.notice_tt LIKE %:#{#filterRequest.noticeTt}%) " +
	        "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
	        "     OR n.notice_wd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate}) ",
	    nativeQuery = true)
    List<Notice> select_FILTERED_NOTICE(@Param("filterRequest") NoticeFilterRequest filterRequest);
	
	
}
