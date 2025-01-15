package com.itwillbs.c4d2412t3p1.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogDTO {

	private String log_cd;
	private String log_sj;
	private String log_ju;
	private Map<String, Object> log_jdMap;
	private String parsedLogDetails;
	private String log_od; // 날짜를 문자열로 처리 (yyyy-MM-dd HH:mm:ss)
	private String log_oi;
	private String log_bj;
	private String employee_id; // Employee 테이블에서 조인한 값
	private String employee_cd;
    private Long sequenceValue;

	// 날짜를 LocalDateTime으로 변환하는 유틸리티 메서드
	public LocalDateTime getLogOdAsLocalDateTime() {
		if (log_od == null || log_od.isEmpty()) {
			return null;
		}
		return LocalDateTime.parse(log_od, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public void setLogOdFromLocalDateTime(LocalDateTime log_od) {
		if (log_od == null) {
			this.log_od = null;
		} else {
			this.log_od = log_od.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	}

}
