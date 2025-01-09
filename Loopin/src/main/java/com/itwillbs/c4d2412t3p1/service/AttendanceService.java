package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
	private final HolidayMapper holidayMapper;

	public List<Attendance> findAll() {
		return attendanceRepository.findAll();
	}

	public void insert_ANNUAL() {
		attendanceMapper.insert_ANNUAL();
	}

	public List<Map<String, Object>> select_EMPLOYEE_ANNUAL(String employee_nm) {
		return attendanceMapper.select_EMPLOYEE_ANNUAL(employee_nm);
	}

	public List<Map<String, Object>> select_ANNUAL(AttendanceDTO attendanceDTO) {
		return attendanceMapper.select_ANNUAL(attendanceDTO);
	}

	public List<Holiday> select_HOLIDAY() {
		return holidayRepository.findAll();
	}

	public List<Map<String, Object>> select_period_HOLIDAY(String holiday_dt1, String holiday_dt2) {
		return holidayMapper.select_period_HOLIDAY(holiday_dt1, holiday_dt2);
	}
	
	public void insertHolidays(List<Map<String, String>> holidays) throws Exception {
		Date now = new Date(); //Date타입으로 변수 선언
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //데이트 포맷
		String date_string = dateFormat.format(now);
        for (Map<String, String> holiday : holidays) {
        	holiday.put("holiday_wd", date_string);
            holidayMapper.insertHoliday(holiday);
        }
    }
	
//	@Transactional
//	public Map<String, Object> insertHolidays(List<HolidayDTO> holidays) {
//
//	    List<Holiday> insertList = new ArrayList<>();
//	    List<HolidayDTO> failedRows = new ArrayList<>();
//
//	    Timestamp time = new Timestamp(System.currentTimeMillis());
//	    
//	    holidays.forEach(data -> {
//	        try {
//	        	Holiday holiday = Holiday.builder()
//	                    .holiday_dt(data.getHoliday_dt())
//	                    .holiday_nm(data.getHoliday_nm())
//	                    .holiday_wa("")
//	                    .holiday_aa("N")
//	                    .holiday_wr(data.getHoliday_wr())
//	                    .holiday_wd(time)
//	                    .holiday_mf("")
//	                    .holiday_md("")
//	                    .build();
//	            insertList.add(holiday);
//	        } catch (Exception e) {
//	            failedRows.add(data);
//	        }
//	    });
//
//	    int insertCount = 0;
//
//	    try {
//	    	insertCount = holidayRepository.saveAll(insertList).size();
//	    } catch (Exception e) {
//	        throw new RuntimeException("무언가 오류가 났어요!!!!!!!!!" + e.getMessage(), e);
//	        
//	    }
//
//	    
//	    Map<String, Object> result = new HashMap<>();
//	    result.put("insertCount", insertCount);
//	    result.put("failedRows", failedRows);
//
//	    return result;
//	}
	

//
//	public void delete_TRANSFER(List<Long> ids) {
//		
//		attendanceRepository.deleteAllById(ids);
//
//	}
}
