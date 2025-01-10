package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.domain.WorktypeDTO;
import com.itwillbs.c4d2412t3p1.entity.Comhistory;
import com.itwillbs.c4d2412t3p1.entity.ComhistoryPK;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.CommutePK;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.entity.common_codePK;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;
import com.itwillbs.c4d2412t3p1.mapper.CommuteMapper;
import com.itwillbs.c4d2412t3p1.repository.ComhistoryRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.CommuteRepository;
import com.itwillbs.c4d2412t3p1.repository.HolidayRepository;
import com.itwillbs.c4d2412t3p1.repository.WorkinghourRepository;

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
	private final ComhistoryRepository comhistoryRepository;
	private final HolidayRepository holidayRepository;
	
	private final CommuteMapper commuteMapper; 
	
	


    public List<Commute> select_COMMUTE_detail(String commute_wd) {
    	
        if (commute_wd == null || commute_wd.isEmpty()) {
            return commuteRepository.findAll();
        } else {
            return commuteRepository.select_COMMUTE_detail(commute_wd);
        }
    }



	public List<CommuteDTO> select_COMMUTE_list() {
		return commuteRepository.select_COMMUTE_list();
	}


	// 근로관리 그리드 조회
	public List<WorkinghourDTO> select_WORKINGHOUR() {
		
		return commuteMapper.select_WORKINGHOUR();
	}
	
	// 공통코드 조회
	public List<Common_codeDTO> select_COMMON_list(String common_gc) {
		
		return commonRepository.select_COMMON_list(common_gc);
	}

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



	// 출근하기
	public int insert_COMMUTE(EmployeeDetails employee, Commute commuteEntity ) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp regDate = new Timestamp(System.currentTimeMillis());
		LocalDate today = LocalDate.now();
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String formattedTime = time.format(formatter);

		String employee_cd = employee.getEmployee_cd();
		String workinghour_id = employee.getWorkinghour_id();
		
		if(commuteEntity != null) { // 업데이트
		
			commuteEntity.setCommute_ld(today.toString());
			commuteEntity.setCommute_lt(formattedTime);
			commuteEntity.setCommute_uu(regUser);
			commuteEntity.setCommute_ud(regDate);
			
            return commuteRepository.save(commuteEntity) != null ? 1 : 0;
		} else { // 인서트
			Commute commute = Commute.builder()
					.employee_cd(employee_cd)
					.commute_wd(today.toString())
					.workinghour_id(workinghour_id)
					.commute_wt(formattedTime)
					.commute_ru(regUser)
					.commute_rd(regDate)
					.build();
			return commuteRepository.save(commute) != null ? 1 : 0;
		}
	}


	// 출퇴근 기록 찾기
	public Commute findById(EmployeeDetails employeeDetails) {
		String employee_cd = employeeDetails.getEmployee_cd();
		LocalDate today = LocalDate.now();
		String workinghour_id = employeeDetails.getWorkinghour_id();
		
		return commuteRepository.findById(new CommutePK(employee_cd, workinghour_id, today.toString())).orElse(null);
	}
	
	
	// 로직 수정 테스트 시작 ----------------------------------------------
	public int insert_COMMUTE(String employee_cd, String workinghour_id, Commute commuteEntity ) {
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
			
			if(commuteEntity.getCommute_lt() == null || commuteEntity.getCommute_lt().equals("")) {
				// 출근
				
			} else {
				
			}
			
            return commuteRepository.save(commuteEntity) != null ? 1 : 0;
		} else { // 인서트
			Commute commute = Commute.builder()
					.employee_cd(employee_cd)
					.commute_wd(today.toString())
					.workinghour_id(workinghour_id)
					.commute_wt(formattedTime)
					.commute_ru(regUser)
					.commute_rd(regDate)
					.build();
			return commuteRepository.save(commute) != null ? 1 : 0;
		}
	}
	
	public Commute findById(String employee_cd, String workinghour_id) {
		String today = LocalDate.now().toString();
		return commuteRepository.findById(new CommutePK(employee_cd, workinghour_id, today)).orElse(null);
	}
	
	// 로직 수정 테스트 끝 ----------------------------------------------
	

	// 근로 요일은 일반근무, 연장근무, 야간근무
	// 주휴요일은 휴일근무
	// 근로, 주휴가 아니면 주말근무
	// 공휴일은 휴일근무
	// 연장근무 하루 8시간 5일이면 40시간
	// 주 40시간보다 이상이면 연장근무, 남아서 야근하면 야근수당
	// 야간근무 -> 오후 10시 ~ 오전 6시
	// 사원 근무 기록 등록
	public void insert_COMHISTORY(String employee_cd, String commute_wt, String commute_lt, String workinghour_id) {
	    int currentDay = LocalDate.now().getDayOfWeek().getValue(); // 현재 요일
	    LocalDate date = LocalDate.now(); // 현재 날짜
	    
	 // 이번 주 월요일 구하기
	    LocalDate mondayOfWeek = date.minusDays(currentDay - 1);
	    // 이번 주 일요일 구하기
	    LocalDate sundayOfWeek = mondayOfWeek.plusDays(6);

	    DateTimeFormatter weekformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String startDate = mondayOfWeek.format(weekformatter);
	    String endDate = sundayOfWeek.format(weekformatter);
	    // Repository에서 데이터 조회
	    List<Commute> weeklyCommutes = commuteRepository.findCommutesBetweenDates(
	    		startDate,
	    		endDate
	    );
	    
	 // 주간 총 근무시간 계산
	    double totalWeeklyHours = weeklyCommutes.stream()
	        .mapToDouble(commute -> {
	            // 각 근무 기록의 시간 계산 로직
	            LocalDateTime startTime = LocalDateTime.of(LocalDate.parse(commute.getCommute_wd()),LocalTime.parse(commute.getCommute_wt()));
	            LocalDateTime endTime = LocalDateTime.of(LocalDate.parse( commute.getCommute_ld()), LocalTime.parse(commute.getCommute_lt()));
	            return ChronoUnit.MINUTES.between(startTime, endTime) / 60.0;
	        })
	        .sum();
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
	    String formattedDate = date.format(formatter);
	    // 공휴일유무
	    boolean isHoliday = holidayRepository.findById(formattedDate).orElse(null) != null ? true : false;
	    
	    Workinghour workinghour = workinghourRepository.findById(workinghour_id).orElse(null);
	    String workinghour_hs = workinghour.getWorkinghour_hs(); // 주휴요일
	    
	    // 출근 시간과 퇴근 시간을 LocalTime으로 파싱
	    LocalTime startTime = LocalTime.parse(commute_wt);
	    LocalTime endTime = LocalTime.parse(commute_lt);
	    LocalTime companyStartTime = LocalTime.parse(workinghour.getWorkinghour_wt());
	 // 실제 근무 시작 시간 결정
	    LocalTime actualStartTime = startTime.isBefore(companyStartTime) ? companyStartTime : startTime;
	    
	    // 근로요일 배열로 변환 및 현재 요일과 비교
	    boolean isWorkingDay = Arrays.stream(workinghour.getWorkinghour_dw().split(","))
	            .map(String::trim)
	            .mapToInt(Integer::parseInt)
	            .anyMatch(day -> day == currentDay);

	    // 로직 시작
	    if(isWorkingDay) { // 근로요일 -> 일반근로, 연장근로, 야간근로
//	    LocalTime endTime = LocalTime.parse("23:16:35");
	    	LocalTime companyEndTime = LocalTime.parse(workinghour.getWorkinghour_lt());    // 회사 퇴근 시간
	    	LocalTime nightWorkStartTime = LocalTime.of(22, 0); // 야간근로 시작 시간 22:00
	    	
	        String regularHours = "0.00";
	        String overtimeHours = "0.00";
	        String nightHours = "0.00";
	        
	        // 현재 근무시간 계산
	        long currentWorkMinutes = ChronoUnit.MINUTES.between(actualStartTime, endTime);
	        double currentWorkHours = currentWorkMinutes / 60.0 >= 8.00 ? 8.00 : currentWorkMinutes / 60.0;
	        
	        // 40시간 초과 여부 확인 및 처리
	        if (totalWeeklyHours >= 40.0) {
	            // 이미 40시간 초과한 경우 모든 시간을 연장/야간으로 처리
	            if (endTime.isAfter(nightWorkStartTime)) {
	                // 야간근무 시간 계산
	                long nightMinutes = ChronoUnit.MINUTES.between(nightWorkStartTime, endTime);
	                nightHours = String.format("%.2f", nightMinutes / 60.0);
	                
	                // 연장근무 시간 계산 (시작시간부터 야간근무 시작시간까지)
	                long overtimeMinutes = ChronoUnit.MINUTES.between(startTime, nightWorkStartTime);
	                overtimeHours = String.format("%.2f", overtimeMinutes / 60.0);
	            } else {
	                // 전체 시간을 연장근무로 처리
	                overtimeHours = String.format("%.2f", currentWorkHours);
	            }
	        } else {
	            // 40시간 미만인 경우
	            double remainingRegularHours = 40.0 - totalWeeklyHours;
	            
	            if (currentWorkHours <= remainingRegularHours) {
	                // 현재 근무시간이 남은 일반근무시간 내에 있는 경우
	                regularHours = String.format("%.2f", currentWorkHours);
	            } else {
	                // 일반근무시간을 초과하는 경우
	                regularHours = String.format("%.2f", remainingRegularHours);
	                double overtimeWorkHours = currentWorkHours - remainingRegularHours;
	                
	                if (endTime.isAfter(nightWorkStartTime)) {
	                    // 야간근무 시간 계산
	                    long nightMinutes = ChronoUnit.MINUTES.between(nightWorkStartTime, endTime);
	                    double nightWorkHours = nightMinutes / 60.0;
	                    nightHours = String.format("%.2f", nightWorkHours);
	                    
	                    // 연장근무 시간 계산 (초과시간 - 야간근무시간)
	                    overtimeHours = String.format("%.2f", overtimeWorkHours - nightWorkHours);
	                } else {
	                    // 전체 초과시간을 연장근무로 처리
	                    overtimeHours = String.format("%.2f", overtimeWorkHours);
	                }
	            }
	        }
	    	
	    	Comhistory com = comhistoryRepository.findById(new ComhistoryPK(employee_cd, formattedDate)).orElse(null);
	    	
	    	if(com != null) {
	    		// 기존 값에 새로운 근무 시간 추가
	    		String currentIg = (com.getComhistory_ig() != null) ? com.getComhistory_ig() : "0";
	    		String currentEg = (com.getComhistory_eg() != null) ? com.getComhistory_eg() : "0";
	    		String currentYg = (com.getComhistory_yg() != null) ? com.getComhistory_yg() : "0";
	    		
	    		double totalRegular = Double.parseDouble(currentIg) + Double.parseDouble(regularHours);
	    		double totalOvertime = Double.parseDouble(currentEg) + Double.parseDouble(overtimeHours);
	    		double totalNight = Double.parseDouble(currentYg) + Double.parseDouble(nightHours);
	    		
	    		com.setComhistory_ig(String.format("%.2f", totalRegular));
	    		com.setComhistory_eg(String.format("%.2f", totalOvertime));
	    		com.setComhistory_yg(String.format("%.2f", totalNight));
	    	} else {
	    		// 새 Comhistory 엔트리 생성
	    		com = Comhistory.builder()
	    				.employee_cd(employee_cd)
	    				.comhistory_ym(formattedDate)
	    				.comhistory_ig(regularHours)
	    				.comhistory_eg(overtimeHours)
	    				.comhistory_yg(nightHours)
	    				.build();
	    	}
	    	
	    	comhistoryRepository.save(com);
	    } else if (Integer.parseInt(workinghour_hs) == currentDay || isHoliday) { // 주휴요일, 공휴일 -> 휴일근로
	        long holidayMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
	        String holidayHours = String.format("%.2f", holidayMinutes / 60.0);
	        
	        Comhistory com = comhistoryRepository.findById(new ComhistoryPK(employee_cd, formattedDate)).orElse(null);
	        
	        if(com != null) {
	            // 기존 값에 주휴시간 추가
	            String currentHg = (com.getComhistory_hg() != null) ? com.getComhistory_jg() : "0";
	            double totalHoliday = Double.parseDouble(currentHg) + Double.parseDouble(holidayHours);
	            com.setComhistory_jg(String.format("%.2f", totalHoliday));
	        } else {
	            // 새 Comhistory 엔트리 생성
	            com = Comhistory.builder()
	                .employee_cd(employee_cd)
	                .comhistory_ym(formattedDate)
	                .comhistory_hg(holidayHours)
	                .build();
	        }
	        
	        comhistoryRepository.save(com);
	    } else { // 근로요일, 주휴요일, 공휴일이 아닌 경우 -> 주말근로
	        long weekendMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
	        String weekendHours = String.format("%.2f", weekendMinutes / 60.0);
	        
	        Comhistory com = comhistoryRepository.findById(new ComhistoryPK(employee_cd, formattedDate)).orElse(null);
	        
	        if(com != null) {
	            // 기존 값에 주말근로시간 추가
	            String currentJg = (com.getComhistory_jg() != null) ? com.getComhistory_jg() : "0";
	            double totalWeekend = Double.parseDouble(currentJg) + Double.parseDouble(weekendHours);
	            com.setComhistory_jg(String.format("%.2f", totalWeekend));
	        } else {
	            // 새 Comhistory 엔트리 생성
	            com = Comhistory.builder()
	                .employee_cd(employee_cd)
	                .comhistory_ym(formattedDate)
	                .comhistory_jg(weekendHours)
	                .build();
	        }
	        
	        comhistoryRepository.save(com);
	    }
	    
	}









	

}
