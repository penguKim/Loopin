package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.domain.WorktypeDTO;
import com.itwillbs.c4d2412t3p1.entity.Comhistory;
import com.itwillbs.c4d2412t3p1.entity.ComhistoryPK;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.CommutePK;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.entity.common_codePK;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;
import com.itwillbs.c4d2412t3p1.mapper.CommuteMapper;
import com.itwillbs.c4d2412t3p1.mapper.HolidayMapper;
import com.itwillbs.c4d2412t3p1.repository.ComhistoryRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.CommuteRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.HolidayRepository;
import com.itwillbs.c4d2412t3p1.repository.WorkinghourRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommuteService {
	
	private final CommonRepository commonRepository;
	private final CommuteRepository commuteRepository;
	private final WorkinghourRepository workinghourRepository;
	private final HolidayRepository holidayRepository;
	private final EmployeeRepository employeeRepository;
	
	private final CommuteMapper commuteMapper; 
	private final HolidayMapper holidayMapper;
	


	// 공통코드 조회
	public List<Common_codeDTO> select_COMMON_list(String common_gc) {
		
		return commonRepository.select_COMMON_list(common_gc);
	}

	// 출퇴근 기록부 --------------------------------------------
	
    // 출퇴근 기록부 달력 조회
	public List<CommuteDTO> select_COMMUTE_calendar(String employee_cd, boolean isAdmin, String startDate, String endDate) {
//		return commuteRepository.select_COMMUTE_calendar(employee_cd, isAdmin, startDate, endDate);
		return commuteMapper.select_COMMUTE_calendar(employee_cd, isAdmin, startDate, endDate);
	}
	
	// 출퇴근 기록부 달력 상세 조회
	public List<CommuteDTO> select_COMMUTE_detail(String employee_cd, boolean isAdmin, String commute_wd) {
		return commuteMapper.select_COMMUTE_detail(employee_cd, isAdmin, commute_wd);
	}
	
	// 출퇴근 기록부 그리드 조회
	public List<CommuteDTO> select_COMMUTE_grid(CommuteFilterRequest filter, String employee_cd, boolean isAdmin) {
		return commuteMapper.select_COMMUTE_grid(filter, employee_cd, isAdmin);
	}
	
	// 근로시간 조회
	public CommuteDTO selcet_COMMUTE_time(CommuteFilterRequest filter, String employee_cd, boolean isAdmin) {
		return commuteMapper.selcet_COMMUTE_time(filter, employee_cd, isAdmin);
	}

	// 근로관리 그리드 조회
	public List<WorkinghourDTO> select_WORKINGHOUR() {
		
		return commuteMapper.select_WORKINGHOUR();
	}
	
	// 미출근 사원 조회
	public List<CommuteDTO> select_EMPLOYEE_grid(String commute_wd) {
		
		return commuteMapper.select_EMPLOYEE_grid(commute_wd);
	}
	
	// 출근일정 조회의 등록
	public void insert_COMMUTE_modal(CommuteDTO commuteDTO) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    Commute com = commuteRepository.findById(new CommutePK(commuteDTO.getEmployee_cd(), 
	    		commuteDTO.getWorkinghour_id(), commuteDTO.getCommute_wd())).orElse(null);
	    // 출퇴근 시간 비교
	    if (!commuteDTO.getCommute_lt().isEmpty()) {
	        String workTime = commuteDTO.getCommute_wt().replace(":", "");
	        String leaveTime = commuteDTO.getCommute_lt().replace(":", "");
	        if (Integer.parseInt(leaveTime) < Integer.parseInt(workTime)) {
	            LocalDate workDate = LocalDate.parse(commuteDTO.getCommute_wd());
	            commuteDTO.setCommute_ld(workDate.plusDays(1).toString());
	        } else {
	            commuteDTO.setCommute_ld(commuteDTO.getCommute_wd());
	        }
	    }
	    
	    if (com != null) { // 업데이트
	    	com.setCommute_wd(commuteDTO.getCommute_wd());
	    	com.setCommute_wt(commuteDTO.getCommute_wt());
	    	com.setCommute_uu(regUser);
	    	com.setCommute_ud(time);
	    	if (commuteDTO.getCommute_lt().equals("")) {
	    		com.setCommute_ld("");
	    		com.setCommute_lt("");
	    		com.setCommute_ig("");
	    		com.setCommute_eg("");
	    		com.setCommute_yg("");
	    		com.setCommute_jg("");
	    		com.setCommute_hg("");
	    	} else {
	    		com.setCommute_ld(commuteDTO.getCommute_ld());
	    		com.setCommute_lt(commuteDTO.getCommute_lt());
	    		setCOMMUTE_hour(com, true);
	    	}
	    	
	    	commuteRepository.save(com);
	    } else {
	    	commuteDTO.setCommute_ru(regUser);
	    	commuteDTO.setCommute_rd(time);
	    	
	    	commuteRepository.save(Commute.setCommute(commuteDTO));
	    }
	}


	// 공휴일 조회
	public List<Holiday> select_HOLIDAY_month(String calendarStartDate, String calendarEndDate) {
	    return holidayRepository.findHolidaysInMonth(calendarStartDate, calendarEndDate);
	}
	
	
	
	// 근로 관리 --------------------------------------------

	// 근로관리 상세 항목 조회
	public Workinghour select_WORKINGHOUR_detail(String workinghour_id) {
		return workinghourRepository.findById(workinghour_id).orElseThrow(() -> 
		new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
	}

	// 근로관리 항목 등록
	public Workinghour insert_WORKINGHOUR(WorkinghourDTO workinghourDTO) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
	    Optional<Workinghour> presentWorkinghour = workinghourRepository.findById(workinghourDTO.getWorkinghour_id());
	    
	    if(presentWorkinghour.isPresent()) {
	    	Workinghour present = presentWorkinghour.get();
	        workinghourDTO.setWorkinghour_ru(present.getWorkinghour_ru());
	        workinghourDTO.setWorkinghour_rd(present.getWorkinghour_rd());
	        workinghourDTO.setWorkinghour_uu(regUser);
	        workinghourDTO.setWorkinghour_ud(time);
	    } else {
	        workinghourDTO.setWorkinghour_ru(regUser);
	        workinghourDTO.setWorkinghour_rd(time);
	    }
	    
		return workinghourRepository.save(Workinghour.setWorkinghour(workinghourDTO));
	}

	// 사원추가 모달 - 선택안된 직원 조회
	public List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR(String workinghour_id) {
		return commuteMapper.select_EMPLOYEE_WORKINGHOUR(workinghour_id);
	}

	// 사원추가 모달 - 선택된 직원 조회
	public List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR_CHK(String workinghour_id) {
		return commuteMapper.select_EMPLOYEE_WORKINGHOUR_CHK(workinghour_id);
	}

	// 사원의 근로 등록
	@Transactional
	public int update_EMPLOYEE_WK(List<WorkinghourDTO> list, String workinghour_id) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
		int count = 0;
		try {
			count = 0;
			for(WorkinghourDTO row : list) {
				row.setWorkinghour_id(row.getWorkinghour_id() == null ? workinghour_id : "");
				row.setWorkinghour_uu(regUser);
				row.setWorkinghour_ud(time);
				int updcount = commuteMapper.update_EMPLOYEE_WK(row);
				System.out.println("UPDCOUNT : " + updcount);
				if(updcount > 0) {
					count++;				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	// 출근 -------------------------------------
	// 출퇴근 기록 찾기
	public Commute findById(String employee_cd, String workinghour_id, String date) {

		return commuteRepository.findById(new CommutePK(employee_cd, workinghour_id, date)).orElse(null);
	}
	
	// 출근하기
	public Commute insert_COMMUTE(String employee_cd, String workinghour_id, Commute commuteEntity ) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp regDate = new Timestamp(System.currentTimeMillis());
		LocalDate today = LocalDate.now();
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String formattedTime = time.format(formatter);

		if(commuteEntity != null) { // 업데이트
		
			commuteEntity.setCommute_ld(today.toString());
			commuteEntity.setCommute_lt(formattedTime);
			commuteEntity.setCommute_uu(regUser);
			commuteEntity.setCommute_ud(regDate);
			
            // 근로시간 계산 및 설정
            setCOMMUTE_hour(commuteEntity, false);
            
            return commuteRepository.save(commuteEntity);
            
		} else { // 인서트
			Commute commute = Commute.builder()
					.employee_cd(employee_cd)
					.commute_wd(today.toString())
					.workinghour_id(workinghour_id)
					.commute_wt(formattedTime)
					.commute_ru(regUser)
					.commute_rd(regDate)
					.build();
			return commuteRepository.save(commute);
		}
	}
	
	// 출근하기
	public Commute insert_COMMUTE_list(String employee_cd, String workinghour_id, Commute commuteEntity ) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp regDate = new Timestamp(System.currentTimeMillis());
		LocalDate today = LocalDate.now();
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String formattedTime = time.format(formatter);

		if(commuteEntity != null) { // 업데이트
		
//			commuteEntity.setCommute_ld(today.toString());
//			commuteEntity.setCommute_lt(formattedTime);
			commuteEntity.setCommute_ld("2025-01-24");
			commuteEntity.setCommute_lt("20:00:00");
			commuteEntity.setCommute_uu(regUser);
			commuteEntity.setCommute_ud(regDate);
			
            // 근로시간 계산 및 설정
            setCOMMUTE_hour(commuteEntity, false);
            
            return commuteRepository.save(commuteEntity);
            
		} else { // 인서트
			Commute commute = Commute.builder()
					.employee_cd(employee_cd)
					.workinghour_id(workinghour_id)
//					.commute_wd(today.toString())
//					.commute_wt(formattedTime)
					.commute_wd("2025-01-24")
					.commute_wt("09:00:00")
					.commute_ru(regUser)
					.commute_rd(regDate)
					.build();
			return commuteRepository.save(commute);
		}
	}
	
	
	// 출퇴근 현황 --------------------------------------------

	// 현황 그리드 조회
	public List<CommuteDTO> select_COMMUTE_timeList(CommuteFilterRequest filter) {
		return commuteMapper.select_COMMUTE_timeList(filter);
	}
	
	
	// 서비스에서 사용 -----------------------------------------------------
	
	// 근로 요일은 일반근무, 연장근무, 야간근무
	// 주휴요일은 휴일근무
	// 근로, 주휴가 아니면 주말근무
	// 공휴일은 휴일근무
	// 일반근무 하루 8시간 5일이면 40시간
	// 주 40시간보다 이상이면 연장근무, 남아서 야근하면 야근수당
	// 야간근무 -> 오후 10시 ~ 오전 6시
	// 사원 근무 기록 등록
	private void setCOMMUTE_hour(Commute commute, boolean isUpdate) {
		String workinghour_id = commute.getWorkinghour_id();
		String commute_wd = commute.getCommute_wd();
		String commute_wt = commute.getCommute_wt();
		String commute_ld = commute.getCommute_ld();
		String commute_lt = commute.getCommute_lt();
		if(isUpdate) {
			commute.setCommute_ig("0");
			commute.setCommute_eg("0");
			commute.setCommute_yg("0");
			commute.setCommute_jg("0");
			commute.setCommute_hg("0");
		}
	    // 변수 계산 -------------------------------------------------------------------
		DateTimeFormatter yMdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate workDate = LocalDate.parse(commute.getCommute_wd()); // 출근일자
	    int currentDayValue = workDate.getDayOfWeek().getValue(); // 출근일자 값
	    
	    LocalDate monday = workDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 출근일자 주의 월요일
	    String startDate = monday.format(yMdFormatter);
	    LocalDate previousDay = workDate.getDayOfWeek() == DayOfWeek.MONDAY ? workDate : workDate.minusDays(1);
	    String endDate = previousDay.format(yMdFormatter);
	    double totalWeeklyHours = commuteRepository.sumCommuteIgByDateRange(commute.getEmployee_cd(), startDate, endDate);
	    boolean isHoliday = holidayMapper.isHoliday(workDate.format(yMdFormatter));
	    
	    Workinghour workinghour = workinghourRepository.findById(workinghour_id).orElse(null); // 근로형태 조회
	    String workinghour_hs = workinghour.getWorkinghour_hs(); // 주휴요일
	    
	    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.parse(commute_wt)); // 출근일시
	    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.parse(commute_ld), LocalTime.parse(commute_lt)); // 퇴근일시
	    LocalDateTime companyStartDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.parse(workinghour.getWorkinghour_wt())); // 근로형태 출근일시
	    LocalDateTime actualStartDateTime = startDateTime.isBefore(companyStartDateTime) ? companyStartDateTime : startDateTime; // 사원 출근일시
	    // 근로요일 판별
	    boolean isWorkingDay = Arrays.stream(workinghour.getWorkinghour_dw().split(","))
	        .map(String::trim)
	        .mapToInt(Integer::parseInt)
	        .anyMatch(day -> day == currentDayValue);

	    if(isWorkingDay && !isHoliday) { // 근로요일이며 공휴일이 아닐 경우 경우
	        LocalDateTime companyEndDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.parse(workinghour.getWorkinghour_lt())); // 근로형태 퇴근일시
	        LocalDateTime nightWorkStartDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.of(22, 0)); // 야간근무 시간
	        
	        // 점심시간
	        LocalDateTime lunchStartDateTime = LocalDateTime.of(workDate, LocalTime.of(12, 0));
	        LocalDateTime lunchEndDateTime = LocalDateTime.of(workDate, LocalTime.of(13, 0));
	        
	        double regularWorkHours; // 실제 근무시간
	        if (endDateTime.isBefore(lunchStartDateTime)) { // 점심시간 전에 퇴근
	            regularWorkHours = Duration.between(actualStartDateTime, endDateTime).toMinutes() / 60.0;
	        } else if (actualStartDateTime.isAfter(lunchEndDateTime)) { // 점심시간 후에 출근
	            regularWorkHours = Duration.between(actualStartDateTime, endDateTime).toMinutes() / 60.0;
	        } else { // 점심시간이 포함된 경우
	            double morningHours = actualStartDateTime.isBefore(lunchStartDateTime) ?
	                Duration.between(actualStartDateTime, lunchStartDateTime).toMinutes() / 60.0 : 0;
	            double afternoonHours = endDateTime.isAfter(lunchEndDateTime) ?
	                Duration.between(lunchEndDateTime, endDateTime).toMinutes() / 60.0 : 0;
	            regularWorkHours = morningHours + afternoonHours;
	        }
	        regularWorkHours = Math.min(regularWorkHours, 8.00);
	        
	        if (totalWeeklyHours >= 40.0) { // 주 40시간 이상(연장, 야간으로 적용)
	            if (endDateTime.isAfter(nightWorkStartDateTime)) { // 야간 근로 시간 이후 퇴근
	            	// 연장근무(근로형태퇴근시간, 22시)
	            	double overtimeMinutes = Duration.between(companyEndDateTime, nightWorkStartDateTime).toMinutes();
	            	commute.setCommute_eg(String.format("%.2f", overtimeMinutes / 60.0));
	            	// 야간근무(22시, 퇴근시간)
	                double nightMinutes = Duration.between(nightWorkStartDateTime, endDateTime).toMinutes();
	                commute.setCommute_yg(String.format("%.2f", nightMinutes / 60.0));
	                
	            } else if (endDateTime.isAfter(companyEndDateTime)) { // 근로형태 퇴근 시간 이후 퇴근
	            	// 연장근무(근로형태퇴근시간, 퇴근시간)
	                double overtimeMinutes = Duration.between(companyEndDateTime, endDateTime).toMinutes();
	                commute.setCommute_eg(String.format("%.2f", overtimeMinutes / 60.0));
	            }
	        } else { // 주 40시간 미만(일반, 연장, 야간으로 적용)
	            double remainingRegularHours = 40.0 - totalWeeklyHours; // 주당 남은 일반근로시간
	            double overtimeWorkHours = 0.0;
	            
	    	    if (regularWorkHours <= remainingRegularHours) { // 근무시간이 더 작은 경우
	    	        commute.setCommute_ig(String.format("%.2f", regularWorkHours)); // 근무시간으로 적용
	    	    } else {
	    	        commute.setCommute_ig(String.format("%.2f", remainingRegularHours)); // 주당 남은 근로시간으로 적용
	    	        overtimeWorkHours = regularWorkHours - remainingRegularHours; // 초과 시간
	            
	    	    }
	                
                if (endDateTime.isAfter(nightWorkStartDateTime)) { // 야간 근로 시간 이후 퇴근
                 // 야간근무(22시, 퇴근시간)
                    double nightMinutes = Duration.between(nightWorkStartDateTime, endDateTime).toMinutes();
                    double nightWorkHours = nightMinutes / 60.0;
                    commute.setCommute_yg(String.format("%.2f", nightWorkHours));
                    // 연장근무 시간 계산 (초과시간 - 야간근무시간)
                    double overtimeMinutes = Duration.between(companyEndDateTime, endDateTime).toMinutes();
    	            commute.setCommute_eg(String.format("%.2f", overtimeWorkHours - nightWorkHours + (overtimeMinutes / 60.0)));
                    
                } else if (endDateTime.isAfter(companyEndDateTime)) { // 근로형태 퇴근시간 이후 퇴근
                	double overtimeMinutes = Duration.between(companyEndDateTime, endDateTime).toMinutes();
                    commute.setCommute_eg(String.format("%.2f", overtimeWorkHours + (overtimeMinutes / 60.0)));
                }
	        }
	        
	        
	    } else if (Integer.parseInt(workinghour_hs) == currentDayValue || isHoliday) {
	        double holidayMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
	        commute.setCommute_hg(String.format("%.2f", holidayMinutes / 60.0));
	        
	    } else {
	        double weekendMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
	        commute.setCommute_jg(String.format("%.2f", weekendMinutes / 60.0));
	    }
	    
	}



	
	
	
	
	
	
	
	// ------------------ 공통으로 뺄것들 -------------------
	
	public EmployeeDetails getEmployee() {
		return (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	
	public boolean isAuthority(String... authorities) {
	    Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	            
	    for (String authority : authorities) {
	        String roleAuthority = "ROLE_" + authority;
	        for (GrantedAuthority userAuth : userAuthorities) {
	            if (userAuth.getAuthority().equals(roleAuthority)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	public boolean isNotAuthority(String... authorities) {
	    Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	            
	    for (String authority : authorities) {
	        String roleAuthority = "ROLE_" + authority;
	        for (GrantedAuthority userAuth : userAuthorities) {
	            if (userAuth.getAuthority().equals(roleAuthority)) {
	                return false;
	            }
	        }
	    }
	    return true;
	}

	// 출퇴근 차트
	public List<CommuteDTO> select_COMMUTE_commuteChart(CommuteFilterRequest filter) {
		System.out.println("---------------------서비스");

		return commuteMapper.select_COMMUTE_commuteChart(filter);
	}
	
	public Map<String, Object> createSeriesData(String name, Function<CommuteDTO, Object> mapper, List<CommuteDTO> data) {
	    Map<String, Object> series = new HashMap<>();
	    series.put("name", name);
	    series.put("data", data.stream().map(mapper).collect(Collectors.toList()));
	    return series;
	}

	public List<String> select_EMPLOYEE_CD_list() {
		return commuteMapper.select_EMPLOYEE_CD_list();
	}








	
	
	
	
	
	

//	public int insert_COMMUTE_hour(String employee_cd, String commute_wd, String commute_ld, String commute_wt, String commute_lt, String workinghour_id) {
//	    DateTimeFormatter yMdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//	    DateTimeFormatter yMformatter = DateTimeFormatter.ofPattern("yyyy-MM");
//	    // 변수 계산 -------------------------------------------------------------------
//	    LocalDate workDate = LocalDate.parse(commute_wd); // 출근일자
//	    int currentDayValue = workDate.getDayOfWeek().getValue(); // 출근일자 값
//	    
//	    LocalDate monday = workDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // 출근일자 주의 월요일
//	    String startDate = monday.format(yMdFormatter);
//	    LocalDate previousDay = workDate.getDayOfWeek() == DayOfWeek.MONDAY ? workDate : workDate.minusDays(1);
//	    String endDate = previousDay.format(yMdFormatter);
//	    System.out.println("----------------------employee_cd : " + employee_cd);
//	    System.out.println("----------------------startDate : " + startDate);
//	    System.out.println("----------------------endDate : " + endDate);
//	    double totalWeeklyHours = commuteRepository.sumCommuteIgByDateRange(employee_cd, startDate, endDate);
//	    System.out.println("----------------------근로시간 : " + totalWeeklyHours);
//	    String comhistory_ym = workDate.format(yMformatter); // 년-월(근로시간 테이블용)
//	    boolean isHoliday = holidayRepository.findById(workDate.format(yMdFormatter)).orElse(null) != null; // 공휴일 유무
//	    
//	    Workinghour workinghour = workinghourRepository.findById(workinghour_id).orElse(null); // 근로형태 조회
//	    String workinghour_hs = workinghour.getWorkinghour_hs(); // 주휴요일
//	    
//	    LocalDateTime startDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.parse(commute_wt)); // 출근일시
//	    LocalDateTime endDateTime = LocalDateTime.of(LocalDate.parse(commute_ld), LocalTime.parse(commute_lt)); // 퇴근일시
//	    LocalDateTime companyStartDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.parse(workinghour.getWorkinghour_wt())); // 근로형태 출근일시
//	    LocalDateTime actualStartDateTime = startDateTime.isBefore(companyStartDateTime) ? companyStartDateTime : startDateTime; // 사원 출근일시
//	    // 근로요일 판별
//	    boolean isWorkingDay = Arrays.stream(workinghour.getWorkinghour_dw().split(","))
//	        .map(String::trim)
//	        .mapToInt(Integer::parseInt)
//	        .anyMatch(day -> day == currentDayValue);
//
//	    if(isWorkingDay) { // 근로요일일 경우
//	        LocalDateTime companyEndDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.parse(workinghour.getWorkinghour_lt())); // 근로형태 퇴근일시
//	        LocalDateTime nightWorkStartDateTime = LocalDateTime.of(LocalDate.parse(commute_wd), LocalTime.of(22, 0)); // 야간근무 시간
//	        
//	        String regularHours = "0.00"; // 일반근로
//	        String overtimeHours = "0.00"; // 연장근로
//	        String nightHours = "0.00"; // 야간근로
//	        
//	        // 점심시간
//	        LocalDateTime lunchStartDateTime = LocalDateTime.of(workDate, LocalTime.of(12, 0));
//	        LocalDateTime lunchEndDateTime = LocalDateTime.of(workDate, LocalTime.of(13, 0));
//	        
//	        double regularWorkHours; // 실제 근무시간
//	        if (endDateTime.isBefore(lunchStartDateTime)) { // 점심시간 전에 퇴근
//	            regularWorkHours = Duration.between(actualStartDateTime, endDateTime).toMinutes() / 60.0;
//	        } else if (actualStartDateTime.isAfter(lunchEndDateTime)) { // 점심시간 후에 출근
//	            regularWorkHours = Duration.between(actualStartDateTime, endDateTime).toMinutes() / 60.0;
//	        } else { // 점심시간이 포함된 경우
//	            double morningHours = actualStartDateTime.isBefore(lunchStartDateTime) ?
//	                Duration.between(actualStartDateTime, lunchStartDateTime).toMinutes() / 60.0 : 0;
//	            double afternoonHours = endDateTime.isAfter(lunchEndDateTime) ?
//	                Duration.between(lunchEndDateTime, endDateTime).toMinutes() / 60.0 : 0;
//	            regularWorkHours = morningHours + afternoonHours;
//	        }
//	        regularWorkHours = Math.min(regularWorkHours, 8.00);
//	        
//	        if (totalWeeklyHours >= 40.0) { // 주 40시간 이상(연장, 야간으로 적용)
//	            if (endDateTime.isAfter(nightWorkStartDateTime)) { // 야간 근로 시간 이후 퇴근
//	            	// 연장근무(근로형태퇴근시간, 22시)
//	            	double overtimeMinutes = Duration.between(companyEndDateTime, nightWorkStartDateTime).toMinutes();
//	            	overtimeHours = String.format("%.2f", overtimeMinutes / 60.0);
//	            	// 야간근무(22시, 퇴근시간)
//	                double nightMinutes = Duration.between(nightWorkStartDateTime, endDateTime).toMinutes();
//	                nightHours = String.format("%.2f", nightMinutes / 60.0);
//	                
//	            } else if (endDateTime.isAfter(companyEndDateTime)) { // 근로형태 퇴근 시간 이후 퇴근
//	            	// 연장근무(근로형태퇴근시간, 퇴근시간)
//	                double overtimeMinutes = Duration.between(companyEndDateTime, endDateTime).toMinutes();
//	                overtimeHours = String.format("%.2f", overtimeMinutes / 60.0);
//	            }
//	        } else { // 주 40시간 미만(일반, 연장, 야간으로 적용)
//	            double remainingRegularHours = 40.0 - totalWeeklyHours; // 주당 남은 일반근로시간
//	            System.out.println("---------------------- 주당 남은 근로시간 : " + remainingRegularHours);
//	            double overtimeWorkHours = 0.0;
//	            if (regularWorkHours <= remainingRegularHours) { // 근무시간이 더 작은 경우
//	                regularHours = String.format("%.2f", regularWorkHours); // 근무시간으로 적용
//	            } else {
//	                regularHours = String.format("%.2f", remainingRegularHours); // 주당 남은 근로시간으로 적용
//	                overtimeWorkHours = regularWorkHours - remainingRegularHours; // 초과 시간
//	            }
//	            System.out.println("---------------------- 일반근로시간 : " + regularHours);
//	            System.out.println("---------------------- 초과시간 : " + overtimeWorkHours);
//	            
//	            
//	                
//                if (endDateTime.isAfter(nightWorkStartDateTime)) { // 야간 근로 시간 이후 퇴근
//                	System.out.println("---------------------- 야간근로 시간");
//                 // 야간근무(22시, 퇴근시간)
//                    double nightMinutes = Duration.between(nightWorkStartDateTime, endDateTime).toMinutes();
//                    double nightWorkHours = nightMinutes / 60.0;
//                    nightHours = String.format("%.2f", nightWorkHours);
//                    System.out.println("---------------------- 야간근로 시간 : " + nightHours);
//                    
//                    // 연장근무 시간 계산 (초과시간 - 야간근무시간)
//                    double overtimeMinutes = Duration.between(companyEndDateTime, endDateTime).toMinutes();
//                    overtimeHours = String.format("%.2f", overtimeWorkHours - nightWorkHours + (overtimeMinutes / 60.0));
//                    System.out.println("---------------------- 연장근로 시간 : " + overtimeHours);
//                    
//                } else if (endDateTime.isAfter(companyEndDateTime)) { // 근로형태 퇴근시간 이후 퇴근
//                    overtimeHours = String.format("%.2f", overtimeWorkHours); // 초과시간으로 적용
//                }
//	        }
//	        
//	        return insert_COMMUTE_hour_detail(employee_cd, workinghour_id, commute_wd, regularHours, overtimeHours, nightHours, null, null);
//	        
//	    } else if (Integer.parseInt(workinghour_hs) == currentDayValue || isHoliday) {
//	        double holidayMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
//	        String holidayHours = String.format("%.2f", holidayMinutes / 60.0);
//	        
//	        return insert_COMMUTE_hour_detail(employee_cd, workinghour_id, commute_wd, null, null, null, holidayHours, null);
//	        
//	    } else {
//	        double weekendMinutes = Duration.between(startDateTime, endDateTime).toMinutes();
//	        String weekendHours = String.format("%.2f", weekendMinutes / 60.0);
//	        
//	        return insert_COMMUTE_hour_detail(employee_cd, workinghour_id, commute_wd, null, null, null, null, weekendHours);
//	    }
//	}
//
//	// 근로시간 저장
//	private int insert_COMMUTE_hour_detail(String employee_cd, String workinghour_id, String commute_wd, String regularHours, 
//			String overtimeHours, String nightHours, String holidayHours, String weekendHours) {
//	    
//	    Commute com = commuteRepository.findById(new CommutePK(employee_cd, workinghour_id, commute_wd)).get();
//	    
//        com.setCommute_ig(regularHours != null ? regularHours : "0.00");
//        com.setCommute_eg(overtimeHours != null ? overtimeHours : "0.00");
//        com.setCommute_yg(nightHours != null ? nightHours : "0.00");
//        com.setCommute_hg(holidayHours != null ? holidayHours : "0.00");
//        com.setCommute_jg(weekendHours != null ? weekendHours : "0.00");
//	    
//	    return commuteRepository.save(com) !=null ? 1 : 0;
//	}
	
	
	
	
//	private int saveComhistory(String employee_cd, String comhistory_ym, String regularHours, 
//			String overtimeHours, String nightHours, String holidayHours, String weekendHours) {
//	    
//	    Comhistory com = comhistoryRepository.findById(new ComhistoryPK(employee_cd, comhistory_ym))
//	        .orElse(Comhistory.builder()
//	            .employee_cd(employee_cd)
//	            .comhistory_ym(comhistory_ym)
//	            .build());
//	    
//	    if (regularHours != null) { // 일반근로
//	        String currentIg = (com.getComhistory_ig() != null) ? com.getComhistory_ig() : "0";
//	        double totalRegular = Double.parseDouble(currentIg) + Double.parseDouble(regularHours);
//	        com.setComhistory_ig(String.format("%.2f", totalRegular));
//	    }
//	    if (overtimeHours != null) { // 연장근로
//	        String currentEg = (com.getComhistory_eg() != null) ? com.getComhistory_eg() : "0";
//	        double totalOvertime = Double.parseDouble(currentEg) + Double.parseDouble(overtimeHours);
//	        com.setComhistory_eg(String.format("%.2f", totalOvertime));
//	    }
//	    if (nightHours != null) { // 야간근로
//	        String currentYg = (com.getComhistory_yg() != null) ? com.getComhistory_yg() : "0";
//	        double totalNight = Double.parseDouble(currentYg) + Double.parseDouble(nightHours);
//	        com.setComhistory_yg(String.format("%.2f", totalNight));
//	    }
//	    if (holidayHours != null) { // 휴일근로
//	        String currentHg = (com.getComhistory_hg() != null) ? com.getComhistory_hg() : "0";
//	        double totalHoliday = Double.parseDouble(currentHg) + Double.parseDouble(holidayHours);
//	        com.setComhistory_hg(String.format("%.2f", totalHoliday));
//	    }
//	    if (weekendHours != null) { // 주말근로
//	        String currentJg = (com.getComhistory_jg() != null) ? com.getComhistory_jg() : "0";
//	        double totalWeekend = Double.parseDouble(currentJg) + Double.parseDouble(weekendHours);
//	        com.setComhistory_jg(String.format("%.2f", totalWeekend));
//	    }
//	    
//	    return comhistoryRepository.save(com) !=null ? 1 : 0;
//	}









	

}