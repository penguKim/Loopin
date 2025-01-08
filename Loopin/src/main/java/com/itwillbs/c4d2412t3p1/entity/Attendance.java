package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.domain.MemberDTO;

import groovy.transform.builder.Builder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "ANNUAL")
@Getter
@Setter
@ToString
@Builder
public class Attendance {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annual_seq_generator")
	@SequenceGenerator(name = "annual_seq_generator", sequenceName = "ANNUAL_SEQ", allocationSize = 1)
	@Column(name = "annual_id", nullable = false)
	private String annual_id;
	@Column(name = "employee_cd")
	private String employee_cd;
	@Column(name = "annual_cc")
	private String annual_cc;
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
	public Attendance() {
	}
	
	public Attendance(String annual_id, String employee_cd, String annual_cc, 
					  String annual_yr, String annual_ua, String annual_ra, String annual_aa,
					  String annual_ru, String annual_rd, String annual_uu, String annual_ud) {
		super();
		this.annual_id = annual_id;
		this.employee_cd = employee_cd;
		this.annual_cc = annual_cc;
		this.annual_yr = annual_yr;
		this.annual_ua = annual_ua;
		this.annual_ra = annual_ra;
		this.annual_aa = annual_aa;
		this.annual_ru = annual_ru;
		this.annual_rd = annual_rd;
		this.annual_uu = annual_uu;
		this.annual_ud = annual_ud;
	}
	
	public static Attendance setAttendanceEntity(AttendanceDTO attendanceDTO) {
		Attendance attendance = new Attendance();
		attendance.setAnnual_id(attendanceDTO.getAnnual_id());
		attendance.setEmployee_cd(attendanceDTO.getEmployee_cd());
		attendance.setAnnual_cc(attendanceDTO.getAnnual_cc());
		attendance.setAnnual_yr(attendanceDTO.getAnnual_yr());
		attendance.setAnnual_ua(attendanceDTO.getAnnual_ua());
		attendance.setAnnual_ra(attendanceDTO.getAnnual_ra());
		attendance.setAnnual_aa(attendanceDTO.getAnnual_aa());
		attendance.setAnnual_ru(attendanceDTO.getAnnual_ru());
		attendance.setAnnual_rd(attendanceDTO.getAnnual_rd());
		attendance.setAnnual_uu(attendanceDTO.getAnnual_uu());
		attendance.setAnnual_ud(attendanceDTO.getAnnual_ud());
		
		return attendance;
	}
	
}
