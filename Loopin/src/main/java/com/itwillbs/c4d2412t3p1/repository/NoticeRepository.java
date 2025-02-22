package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Notice;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.NoticeFilterRequest;

public interface NoticeRepository extends JpaRepository<Notice, String>{
	
	@Query(value = "SELECT NT_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	
	
	
	
	
	@Query(value = "SELECT n.notice_cd, " +
            "n.notice_tt, " +
            "n.notice_ct, " +
            "n.notice_wr, " +
            "n.notice_wd, " +
            "n.notice_mf, " +
            "n.notice_md, " +
            "e.employee_nm, " +
            "d.common_nm AS employee_dp " +
            "FROM NOTICE n " +
            "LEFT JOIN EMPLOYEE e ON n.notice_wr = e.employee_id " +
            "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT' " +
            "WHERE (:#{#filterRequest.employeeDp} IS NULL OR d.common_nm LIKE %:#{#filterRequest.employeeDp}%) " +
            "AND (:#{#filterRequest.employeeNm} IS NULL OR e.employee_nm LIKE %:#{#filterRequest.employeeNm}%) " +
            "AND (:#{#filterRequest.noticeTt} IS NULL OR n.notice_tt LIKE %:#{#filterRequest.noticeTt}%) " +
            "AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL " +
            "     OR n.notice_wd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})", nativeQuery = true)
	List<Object[]> select_FILTERED_NOTICE(@Param("filterRequest") NoticeFilterRequest filterRequest);







	@Query(value = "SELECT n.notice_cd, " +
            "n.notice_tt, " +
            "n.notice_ct, " +
            "n.notice_wr, " +  // 확인용으로 추가
            "n.notice_wd, " +
            "n.notice_mf, " +
            "n.notice_md, " +
            "e.employee_nm, " +
            "d.common_nm AS employee_dp " +  // common_code의 common_nm 추가
            "FROM NOTICE n " +
            "LEFT JOIN EMPLOYEE e " +
            "ON n.notice_wr = e.employee_id " +
            "LEFT JOIN common_code d ON e.employee_dp = d.common_cc AND d.common_gc = 'DEPARTMENT'", nativeQuery = true)
	List<Object[]> select_NOTICE_LIST();

	
	
}
