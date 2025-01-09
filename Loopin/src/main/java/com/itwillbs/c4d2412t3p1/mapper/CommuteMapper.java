package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;

import java.util.List;

@Mapper
public interface CommuteMapper {
	
	// 근로관리 그리드 조회
	List<WorkinghourDTO> select_WORKINGHOUR();
	// 사원추가 모달 - 선택안된 직원 조회
	List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR(String workinghour_id);
	// 사원추가 모달 - 선택된 직원 조회
	List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR_CHK(String workinghour_id);
	// 사원의 근로 등록
	int update_EMPLOYEE_WK(@Param("workinghour") WorkinghourDTO workinghour);
    
}
