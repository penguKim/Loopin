package com.itwillbs.c4d2412t3p1.entity;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "ANNUAL")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AttendancePK.class) // 복합 키 설정
public class Attendance {
	
	
	@Id
	@Column(name = "employee_cd")
	private String employee_cd;
	@Column(name = "annual_cc")
	private String annual_cc;
	@Id
	@Column(name = "annual_yr")
	private String annual_yr;
	@Column(name = "annual_ua")
	private String annual_ua;
	@Column(name = "annual_ra")
	private String annual_ra;
	@Column(name = "annual_aa")
	private String annual_aa;
	@Column(name = "annual_ru")
	private String annual_ru;
	@Column(name = "annual_rd")
	private String annual_rd;
	@Column(name = "annual_uu")
	private String annual_uu;
	@Column(name = "annual_ud")
	private String annual_ud;
	
	// 생성자
//	public Attendance() {
//	}
//	
//	public Attendance(String employee_cd, String annual_cc, 
//					  String annual_yr, String annual_ua, String annual_ra, String annual_aa,
//					  String annual_ru, String annual_rd, String annual_uu, String annual_ud) {
//		super();
//		this.employee_cd = employee_cd;
//		this.annual_cc = annual_cc;
//		this.annual_yr = annual_yr;
//		this.annual_ua = annual_ua;
//		this.annual_ra = annual_ra;
//		this.annual_aa = annual_aa;
//		this.annual_ru = annual_ru;
//		this.annual_rd = annual_rd;
//		this.annual_uu = annual_uu;
//		this.annual_ud = annual_ud;
//	}
//	
//	public static Attendance setAttendanceEntity(AttendanceDTO attendanceDTO) {
//		Attendance attendance = new Attendance();
//		attendance.setEmployee_cd(attendanceDTO.getEmployee_cd());
//		attendance.setAnnual_cc(attendanceDTO.getAnnual_cc());
//		attendance.setAnnual_yr(attendanceDTO.getAnnual_yr());
//		attendance.setAnnual_ua(attendanceDTO.getAnnual_ua());
//		attendance.setAnnual_ra(attendanceDTO.getAnnual_ra());
//		attendance.setAnnual_aa(attendanceDTO.getAnnual_aa());
//		attendance.setAnnual_ru(attendanceDTO.getAnnual_ru());
//		attendance.setAnnual_rd(attendanceDTO.getAnnual_rd());
//		attendance.setAnnual_uu(attendanceDTO.getAnnual_uu());
//		attendance.setAnnual_ud(attendanceDTO.getAnnual_ud());
//		
//		return attendance;
//	}
	
	public static Attendance fromDTO(AttendanceDTO dto) {
	    return Attendance.builder()
	            .employee_cd(dto.getEmployee_cd()) // 사원 코드
	            .annual_cc(dto.getAnnual_cc())     // 연차 코드
	            .annual_yr(dto.getAnnual_yr())     // 사용 연도
	            .annual_ua(dto.getAnnual_ua())     // 사용 연차
	            .annual_ra(dto.getAnnual_ra())     // 잔여 연차
	            .annual_aa(dto.getAnnual_aa())     // 총 연차
	            .annual_ru(dto.getAnnual_ru())     // 작성자
	            .annual_rd(dto.getAnnual_rd())     // 작성일
	            .annual_uu(dto.getAnnual_uu())     // 수정자
	            .annual_ud(dto.getAnnual_ud())     // 수정일
	            .build();
	}
	
}
