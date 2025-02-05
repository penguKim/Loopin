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

	
	@Getter
	@Setter
	@ToString
	// 공지사항 전용 필터 클래스
	public static class NoticeFilterRequest extends FilterRequest {
		
		private String employeeNm;
		private String noticeTt;
		private String employeeDp;
		
		public NoticeFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (employeeNm == null || employeeNm.isEmpty())
					&& (noticeTt == null || noticeTt.isEmpty()) && (employeeDp == null || employeeDp.isEmpty()) ;
		}
	}
	
	
	// 출퇴근 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class CommuteFilterRequest extends FilterRequest {
		
		private String employee_cd;
		private String employee_nm;
		private String employee_dp;
		private String employee_gd;
		private String commute_wt;
		private String commute_lt;
		
		public CommuteFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (employee_cd == null || employee_cd.isEmpty())
					&& (employee_nm == null || employee_nm.isEmpty()) && (employee_dp == null || employee_dp.isEmpty())
					&& (employee_gd == null || employee_gd.isEmpty()) && (commute_wt == null || commute_wt.isEmpty())
					&& (commute_lt == null || commute_lt.isEmpty());
		}
	}
	
	// 창고관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class WarehouseFilterRequest extends FilterRequest {
		
		private String warehouse_cd;
		private String warehouse_nm;
		private String warehouse_tp;
		private String warehouse_us;
		
		public WarehouseFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (warehouse_cd == null || warehouse_cd.isEmpty())
					&& (warehouse_nm == null || warehouse_nm.isEmpty()) && (warehouse_tp == null || warehouse_tp.isEmpty());
		}
	}
	
	// 제품관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class ProductFilterRequest extends FilterRequest {
		
		private String product_cd;
		private String product_nm;
		private String product_gc;
		private String product_cc;
		private String product_wh;
		private String product_us;
		
		public ProductFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (product_cd == null || product_cd.isEmpty())
					&& (product_nm == null || product_nm.isEmpty()) && (product_gc == null || product_gc.isEmpty())
					&& (product_cc == null || product_cc.isEmpty()) && (product_wh == null || product_wh.isEmpty())
					&& (product_us == null || product_us.isEmpty());
		}
	}
	
	
	@Getter
	@Setter
	@ToString
	// 공지사항 전용 필터 클래스
	public static class AccountFilterRequest extends FilterRequest {
		
		private String accountCd;
		private String accountNm;
		private String accountPs;
		private String accountDv;
		
		public AccountFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() 
					&& (accountCd == null || accountCd.isEmpty()) && (accountNm == null || accountNm.isEmpty())
					&& (accountPs == null || accountPs.isEmpty()) && (accountDv == null || accountDv.isEmpty());
		}
	}

}

