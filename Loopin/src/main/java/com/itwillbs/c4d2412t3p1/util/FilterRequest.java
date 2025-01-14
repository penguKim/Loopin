package com.itwillbs.c4d2412t3p1.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilterRequest {

	// 공통 필터 조건 (예: 날짜 범위)
	private String startDate; // 시작 날짜
	private String endDate; // 종료 날짜

	// 공통 필터 조건이 비어 있는지 확인
	public boolean isEmpty() {
		return (startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty());
	}
	
	@Getter
	@Setter
	@ToString
	// 로그 전용 필터 클래스
	public static class LogFilterRequest extends FilterRequest {
		
		private String employee_id; // 사용자 ID
		private String log_sj; // 수행한 작업
		private String log_ju; // 정보 유형
		private String log_oi; // IP 주소
		private String log_bj; // 클라이언트 정보

		public LogFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (employee_id == null || employee_id.isEmpty())
					&& (log_sj == null || log_sj.isEmpty()) && (log_ju == null || log_ju.isEmpty())
					&& (log_oi == null || log_oi.isEmpty()) && (log_bj == null || log_bj.isEmpty());
		}

		@Getter
		@Setter
		@ToString
		// 인사 전용 필터 클래스
		public static class EmployeeFilterRequest extends FilterRequest {
			
			private String employeeHd;
			private String employeeCd;
			private String employeeNm;
			private String employeeDp;
			private String employeeGd;
			
			public EmployeeFilterRequest() {
				super();
			}
			// 로그 필터 조건이 비어 있는지 확인
			@Override
			public boolean isEmpty() {
				return super.isEmpty() && (employeeHd == null || employeeHd.isEmpty())
						&& (employeeCd == null || employeeCd.isEmpty()) && (employeeNm == null || employeeNm.isEmpty())
						&& (employeeDp == null || employeeDp.isEmpty()) && (employeeGd == null || employeeGd.isEmpty());
				}
			}

	}
}