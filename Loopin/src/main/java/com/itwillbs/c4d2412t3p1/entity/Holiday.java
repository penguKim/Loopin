package com.itwillbs.c4d2412t3p1.entity;

import com.itwillbs.c4d2412t3p1.domain.HolidayDTO;

import groovy.transform.builder.Builder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "HOLIDAY")
@Getter
@Setter
@ToString
@Builder
public class Holiday {
	
	
	@Id
	@Column(name = "holiday_dt", nullable = false)
	private String holiday_dt;
	@Column(name = "holiday_nm", nullable = false)
	private String holiday_nm;
	@Column(name = "holiday_wa")
	private String holiday_wa;
	@Column(name = "holiday_aa")
	private String holiday_aa;
	@Column(name = "holiday_wr")
	private String holiday_wr;
	@Column(name = "holiday_wd")
	private String holiday_wd;
	@Column(name = "holiday_mf")
	private String holiday_mf;
	@Column(name = "holiday_md")
	private String holiday_md;
	
	
	// 생성자
	public Holiday() {
	}
	
	public Holiday(String holiday_dt, String holiday_nm, String holiday_wa, String holiday_aa, 
					String holiday_wr, String holiday_wd, String holiday_mf, String holiday_md) {
		super();
		this.holiday_dt = holiday_dt;
		this.holiday_nm = holiday_nm;
		this.holiday_wa = holiday_wa;
		this.holiday_aa = holiday_aa;
		this.holiday_wr = holiday_wr;
		this.holiday_wd = holiday_wd;
		this.holiday_mf = holiday_mf;
		this.holiday_md = holiday_md;
	}
	
	public static Holiday setHolidayEntity(HolidayDTO holidayDTO) {
		Holiday holiday = new Holiday();
		holiday.setHoliday_dt(holidayDTO.getHoliday_dt());
		holiday.setHoliday_nm(holidayDTO.getHoliday_nm());
		holiday.setHoliday_wa(holidayDTO.getHoliday_wa());
		holiday.setHoliday_aa(holidayDTO.getHoliday_aa());
		holiday.setHoliday_wr(holidayDTO.getHoliday_wr());
		holiday.setHoliday_wd(holidayDTO.getHoliday_wd());
		holiday.setHoliday_mf(holidayDTO.getHoliday_mf());
		holiday.setHoliday_md(holidayDTO.getHoliday_md());
		
		return holiday;
	}
	
}
