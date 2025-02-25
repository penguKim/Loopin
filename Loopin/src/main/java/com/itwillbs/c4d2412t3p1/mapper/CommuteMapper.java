package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import java.util.List;

@Mapper
public interface CommuteMapper {
	
	// 출퇴근 캘린더 탭 조회
	List<CommuteDTO> select_COMMUTE_calendar(@Param("employee_cd") String employee_cd, @Param("isAdmin") boolean isAdmin, 
			@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("type") String type);
	// 출퇴근 그리드 탭 조회
	List<CommuteDTO> select_COMMUTE_grid(@Param("filter") CommuteFilterRequest filter, @Param("employee_cd") String employee_cd, 
			@Param("isAdmin") boolean isAdmin, @Param("type") String type);
	// 근로시간 조회
	CommuteDTO selcet_COMMUTE_time(@Param("filter") CommuteFilterRequest filter, @Param("employee_cd") String employee_cd, @Param("isAdmin") boolean isAdmin);
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
	// 현황 그리드 조회
	List<CommuteDTO> select_COMMUTE_timeList(@Param("filter") CommuteFilterRequest filter);
	// 일자별 근로시간 차트
	List<CommuteDTO> select_COMMUTE_dayCommuteChart(@Param("filter") CommuteFilterRequest filter);
	// 근로시간 차트
	List<CommuteDTO> select_COMMUTE_commuteChart(@Param("filter") CommuteFilterRequest filter);
	// 직위, 부서별 출근 차트
	List<CommuteDTO> select_COMMUTE_gradeChart(@Param("sort") String sort, @Param("filter") CommuteFilterRequest filter);
	// 근로코드 조회
	List<WorkinghourDTO> select_WORKINGHOUR_CD();
	// 등록된 사원 카운트
	int countEmployeeWorkinghour(@Param("work") String work);
    
	// 임시 -----------
	// 입사인원 코드 조회
	List<String> select_EMPLOYEE_CD_list();
	EmployeeDTO select_EMPLOYEE(String employee_cd);
	
	
	String select_APPROVAL(@Param("employee_cd") String employee_cd, @Param("date") String date);
	
}