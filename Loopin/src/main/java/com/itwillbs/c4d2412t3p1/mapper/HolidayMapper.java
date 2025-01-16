package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.domain.HolidayDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;

@Mapper
public interface HolidayMapper {

	List<Map<String, Object>> select_period_HOLIDAY(@Param("holiday_dt1") String holiday_dt1, @Param("holiday_dt2")String holiday_dt2);
	
	 // 공휴일 삽입
	void insert_HOLIDAY(Map<String, String> holiday);

    // 휴일 데이터 삽입
    void insert_company_HOLIDAY(Map<String, String> holiday);

    // 휴일 데이터 수정
    void update_company_HOLIDAY(Map<String, String> holiday);

    // 공휴일(유급휴일) 판별
	boolean isHoliday(String date);
}