package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import java.util.List;

@Mapper
public interface CommuteMapper {
	
	// 출퇴근 캘린더 탭 조회
	List<CommuteDTO> select_COMMUTE_calendar(@Param("employee_cd") String employee_cd, @Param("isAdmin") boolean isAdmin, @Param("startDate") String startDate, @Param("endDate") String endDate);
	// 출퇴근 그리드 탭 조회
	List<CommuteDTO> select_COMMUTE_grid(@Param("filter") CommuteFilterRequest filter, @Param("employee_cd") String employee_cd, @Param("isAdmin") boolean isAdmin);
	// 일자별 출퇴근 기록 조회
	List<CommuteDTO> select_COMMUTE_detail(@Param("employee_cd") String employee_cd, @Param("isAdmin") boolean isAdmin, @Param("commute_wd") String commute_wd);
	// 미출근 사원 조회
	List<CommuteDTO> select_EMPLOYEE_grid(String commute_wd);
	// 근로관리 그리드 조회
	List<WorkinghourDTO> select_WORKINGHOUR();
	// 사원추가 모달 - 선택안된 직원 조회
	List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR(String workinghour_id);
	// 사원추가 모달 - 선택된 직원 조회
	List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR_CHK(String workinghour_id);
	// 사원의 근로 등록
	int update_EMPLOYEE_WK(@Param("workinghour") WorkinghourDTO workinghour);
	// 출근 등록
	int insert_COMMUTE();
	// 공휴일 판별
	boolean isHoliday(String formattedDate);
    
}