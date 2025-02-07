package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.domain.HolidayDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.mapper.AttendanceMapper;
import com.itwillbs.c4d2412t3p1.mapper.HolidayMapper;
import com.itwillbs.c4d2412t3p1.repository.AttendanceRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.HolidayRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final AttendanceMapper attendanceMapper;
	private final HolidayRepository holidayRepository;
	private final CommonRepository commonRepository;
	private final HolidayMapper holidayMapper;

	public List<Map<String, Object>> select_ATTENDANCE() {
		return attendanceMapper.select_ATTENDANCE();
	}

	public List<Map<String, Object>> select_EMPLOYEE_ANNUAL(String employee_nm) {
		return attendanceMapper.select_EMPLOYEE_ANNUAL(employee_nm);
	}

	public List<Map<String, Object>> select_filer_ANNUAL(AttendanceDTO attendanceDTO) {
		return attendanceMapper.select_filer_ANNUAL(attendanceDTO);
	}

	public List<Holiday> select_HOLIDAY() {
		return holidayRepository.select_HOLIDAY();
	}

	public List<Map<String, Object>> select_period_HOLIDAY(String holiday_dt1, String holiday_dt2) {
		return holidayMapper.select_period_HOLIDAY(holiday_dt1, holiday_dt2);
	}
	
	public void insert_HOLIDAY(List<Map<String, String>> holidays) throws Exception {
		Date now = new Date(); //Date타입으로 변수 선언
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //데이트 포맷
		String date_string = dateFormat.format(now);
		String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
		
		int createdCount = 0;
        for (Map<String, String> holiday : holidays) {
        	holiday.put("holiday_wd", date_string); // 입력 일시
        	holiday.put("holiday_wr", regUser); // 작성자
            try {
            	holidayMapper.insert_HOLIDAY(holiday);
                createdCount++;
            } catch (Exception e) {
                log.info("휴일 데이터 삽입 실패: {}"+ holiday+ e);
            }
        }
    }
	
	public int delete_company_HOLIDAY(List<String> deletedRows) {
		holidayRepository.deleteAllById(deletedRows); // 반환값 없음
		return deletedRows.size(); // 삭제 요청된 항목 수 반환
	}
	
	public int insertCompanyHoliday(List<Map<String, String>> holidays) throws Exception {
        // 현재 날짜와 시간을 포맷팅
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dateFormat.format(now);
        
        String regUser = SecurityContextHolder.getContext().getAuthentication().getName();

        int createdCount = 0;

        for (Map<String, String> holiday : holidays) {
            // 현재 시간 및 기본 값 추가
            holiday.put("holiday_wd", dateString); // 입력 일시
            holiday.put("holiday_wr", regUser); // 작성자

            // 데이터 삽입 호출
            try {
                holidayMapper.insert_company_HOLIDAY(holiday);
                createdCount++;
            } catch (Exception e) {
                log.info("휴일 데이터 삽입 실패: {}"+ holiday+ e);
            }
        }
        return createdCount;
    }

    public int updateCompanyHoliday(List<Map<String, String>> holidays) throws Exception {
        int updatedCount = 0;

        for (Map<String, String> holiday : holidays) {
            try {
                holidayMapper.update_company_HOLIDAY(holiday);
                updatedCount++;
            } catch (Exception e) {
            	log.info("휴일 데이터 수정 실패: {}"+ holiday+ e);
            }
        }
        return updatedCount;
    }

	public void insert_ANNUAL(List<Map<String, Object>> annuals) {
		Date now = new Date(); //Date타입으로 변수 선언
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //데이트 포맷
		String date_string = dateFormat.format(now);
		
		String regUser = SecurityContextHolder.getContext().getAuthentication().getName();

        for (Map<String, Object> annual : annuals) {
        	annual.put("ANNUAL_RD", date_string);
        	annual.put("ANNUAL_RU", regUser); // 입력 일시
            attendanceMapper.insert_ANNUAL(annual);
        }
	}

	
	public List<Map<String, Object>> select_APPROVAL_ANNUAL(Map<String, Object> params) {
		return attendanceMapper.select_APPROVAL_ANNUAL(params);
	}
	
	public List<Map<String, Object>> select_calendar_ANNUAL(Map<String, Object> params) {
		log.info("params" + params);
		return attendanceMapper.select_calendar_ANNUAL(params);
	}

}