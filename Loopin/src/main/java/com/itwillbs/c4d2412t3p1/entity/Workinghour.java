package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "WORKINGHOUR")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workinghour {
	
	@Id
	@Column(name = "workinghour_id")
	private String workinghour_id;
	@Column(name = "workinghour_nm")
	private String workinghour_nm;
	@Column(name = "workinghour_wt")
	private String workinghour_wt;
	@Column(name = "workinghour_lt")
	private String workinghour_lt;
	@Column(name = "workinghour_wp")
	private String workinghour_wp;
//	@Column(name = "workinghour_hs")
//	private String workinghour_hs;
	@Column(name = "workinghour_dw")
	private String workinghour_dw;
	@Column(name = "workinghour_tt")
	private String workinghour_tt;
	@Column(name = "workinghour_hu")
	private String workinghour_hu;
	@Column(name = "workinghour_us")
	private String workinghour_us;
	@Column(name = "workinghour_rm")
	private String workinghour_rm;
	@Column(name = "workinghour_ru")
	private String workinghour_ru;
	@Column(name = "workinghour_rd")
	private Timestamp workinghour_rd;
	@Column(name = "workinghour_uu")
	private String workinghour_uu;
	@Column(name = "workinghour_ud")
	private Timestamp workinghour_ud;
	

	
	public static Workinghour setCommute(WorkinghourDTO work) {
		String week = work.getWeek() != null ? String.join(",", work.getWeek()) : "";
	    return Workinghour.builder()
	    		.workinghour_id(work.getWorkinghour_id())
	    		.workinghour_nm(work.getWorkinghour_nm())
	    		.workinghour_wt(work.getWorkinghour_wt())
	    		.workinghour_lt(work.getWorkinghour_lt())
	    		.workinghour_wp(work.getWorkinghour_wp())
//	    		.workinghour_hs(work.getWorkinghour_hs())
	    		.workinghour_dw(week)
	    		.workinghour_tt(work.getWorkinghour_tt())
	    		.workinghour_hu(work.getWorkinghour_hu())
	    		.workinghour_us(work.getWorkinghour_us())
	    		.workinghour_rm(work.getWorkinghour_rm())
	    		.workinghour_ru(work.getWorkinghour_ru())
	    		.workinghour_rd(work.getWorkinghour_rd())
	    		.workinghour_uu(work.getWorkinghour_uu())
	    		.workinghour_ud(work.getWorkinghour_ud())
	            .build();
	}

	
	
	
}
