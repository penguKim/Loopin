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
	
	// 제품관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class EquipmentFilterRequest extends FilterRequest {
		
		private String equipment_cd;
		private String model_cd;
		private String equipment_nm;
		private String warehouse_cd;
		private String product_cd;
		private String equipment_us;
		
		public EquipmentFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (equipment_cd == null || equipment_cd.isEmpty())
					&& (model_cd == null || model_cd.isEmpty()) && (equipment_nm == null || equipment_nm.isEmpty())
					&& (product_cd == null || product_cd.isEmpty()) && (equipment_us == null || equipment_us.isEmpty()
					&& (warehouse_cd == null || warehouse_cd.isEmpty()));
		}
	}
		
	// 발주관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class OrderFilterRequest extends FilterRequest {
		
		private String orderCd;
		private String accountCd;
		private String orderPs;
		
		public OrderFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (orderCd == null || orderCd.isEmpty())
					&& (accountCd == null || accountCd.isEmpty()) && (orderPs == null || orderPs.isEmpty());
		}
	}

	// 재고관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class StockFilterRequest extends FilterRequest {
		
		private String warehouse_cd;
		private String warearea_cd;
		private String item_gc;
		private String item_cc;
		private String item_nm;
		private String item_cd;
		private String item_sz;
		private String item_cr;
		
		
		public StockFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (warehouse_cd == null || warehouse_cd.isEmpty())
					&& (warearea_cd == null || warearea_cd.isEmpty()) && (item_gc == null || item_gc.isEmpty())
					&& (item_cc == null || item_cc.isEmpty()) && (item_nm == null || item_nm.isEmpty());
		}
	}
	
	// 수주관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class ContractFilterRequest extends FilterRequest {
		
		private String contractCd;
		private String accountCd;
		private String contractPs;
		
		public ContractFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (contractCd == null || contractCd.isEmpty())
					&& (accountCd == null || accountCd.isEmpty()) && (contractPs == null || contractPs.isEmpty());
		}
	}
	
	// 입출고관리 필터 클래스
	@Getter
	@Setter
	@ToString
	public static class InoutFilterRequest extends FilterRequest {
		
		private String inout_io;
		private String inout_tp;
		private String inout_co;
		private String inout_wh;
		private String inout_wa;
		private String item_gc;
		private String item_cc;
		private String item_nm;
		private String employee_cd;
		private String employee_nm;
		
		public InoutFilterRequest() {
			super();
		}
		// 로그 필터 조건이 비어 있는지 확인
		@Override
		public boolean isEmpty() {
			return super.isEmpty() && (inout_io == null || inout_io.isEmpty())
					&& (inout_tp == null || inout_tp.isEmpty()) && (inout_co == null || inout_co.isEmpty())
					&& (inout_wh == null || inout_wh.isEmpty()) && (inout_wa == null || inout_wa.isEmpty())
					&& (item_gc == null || item_gc.isEmpty()) && (item_cc == null || item_cc.isEmpty())
					&& (item_nm == null || item_nm.isEmpty()) && (employee_cd == null || employee_cd.isEmpty())
					&& (employee_nm == null || employee_nm.isEmpty());
		}
	}

}
