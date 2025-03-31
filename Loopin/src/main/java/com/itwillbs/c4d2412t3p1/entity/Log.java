package com.itwillbs.c4d2412t3p1.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "LOG")
@Table(name = "LOG")
@Getter
@Setter
@ToString(exclude = "employee")
@lombok.extern.java.Log
public class Log {

	@Id
	@Column(name = "log_cd", nullable = false)
	private String log_cd;

	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_cd", referencedColumnName = "employee_cd", nullable = true)
	private Employee employee;

	@Column(name = "log_sj", nullable = false)
	private String log_sj;

	@Column(name = "log_ju", length = 255, nullable = true)
	private String log_ju;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "log_jd", columnDefinition = "CLOB", nullable = true)
	private String log_jd;

	@Column(name = "log_od", nullable = false)
	private String log_od;

	@Column(name = "log_oi", nullable = false)
	private String log_oi;

	@Column(name = "log_bj", length = 255, nullable = true)
	private String log_bj;

	// 기본 생성자 (JPA 용)
	public Log() {
	}

	// log_jd를 제외한 생성자 (요약 조회용)
	public Log(String log_cd, String log_sj, String log_ju, String log_od, String log_oi, String log_bj,
			String employee_cd) {
		this.log_cd = log_cd;
		this.log_sj = log_sj;
		this.log_ju = log_ju;
		this.log_od = log_od;
		this.log_oi = log_oi;
		this.log_bj = log_bj;
		if (employee_cd != null) {
			Employee emp = new Employee();
			emp.setEmployee_cd(employee_cd);
			this.employee = emp;
		}
	}

	@PrePersist
	public void prePersist() {
		// 현재 년도 (두 자리 형식)
		String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
		// 시퀀스 값을 네 자리 형식으로 변환
		String formattedSequence = String.format("%04d", sequenceValue);
		// log_cd 생성: YY-0001 형식
		this.log_cd = currentYear + "-" + formattedSequence;
		log.info("this.log_cd : " + this.log_cd);
	}
//	CREATE SEQUENCE LOG_SEQ
//	START WITH 1
//	INCREMENT BY 1
//	NOCACHE;
}
