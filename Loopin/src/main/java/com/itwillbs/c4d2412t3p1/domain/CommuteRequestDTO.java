package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommuteRequestDTO {
	CommuteFilterRequest commuteFilter;
	
	List<WorkinghourDTO> workinghourList;
	CommuteDTO commute;
	
	String workinghour_id; // 근로형태코드
	boolean attendance; // 출근 여부
	String calendarStartDate; // 캘린더 시작일
	String calendarEndDate; // 캘린더 종료일
	String type; // 파라미터 타입(1 = 본인만)
	
	// 전원 출퇴근 임시 변수
	String day;
	String time;
	
}