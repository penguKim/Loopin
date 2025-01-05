package com.itwillbs.c4d2412t3p1.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter; 
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "LOG")
@Getter
@Setter
@ToString
public class Log {

	@Id
	@Column(name = "log_cd", nullable = false)
	private String log_cd;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_cd", referencedColumnName = "employee_cd", nullable = true)
	private Employee employee;

	@Column(name = "log_sj", nullable = false)
	private String log_sj;

	@Column(name = "log_ju", length = 255, nullable = false)
	private String log_ju;

	@Column(name = "log_jd", columnDefinition = "CLOB", nullable = true)
	private String log_jd;

	@Column(name = "log_od", nullable = false)
	private String log_od;

	@Column(name = "log_oi", nullable = false)
	private String log_oi;

	@Column(name = "log_bj", length = 255, nullable = true)
	private String log_bj;

//	DB에 데이터 삽입 되기 전의 메소드 지정
//	로그코드 파싱 (25-0001, 0002....)
//	@PrePersist
//    private void generateLogCd() {
//        if (this.log_cd == null || this.log_cd.isEmpty()) {
//            int year = LocalDateTime.now().getYear() % 100;
//            int sequenceNumber = 1; 
//            this.log_cd = String.format("%02d-%04d", year, sequenceNumber);
//        }
//    }

}
