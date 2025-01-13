package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;

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
@Table(name = "COMMUTE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(CommutePK.class)
@DynamicInsert
@DynamicUpdate
public class Commute {
	
	@Id
	@Column(name = "employee_cd")
	private String employee_cd;
	@Id
	@Column(name = "workinghour_id")
	private String workinghour_id;
	@Id
	@Column(name = "commute_wd")
	private String commute_wd;
	@Column(name = "commute_ld")
	private String commute_ld;
	@Column(name = "commute_wt")
	private String commute_wt;
	@Column(name = "commute_lt")
	private String commute_lt;
	@Column(name = "commute_ig")
	private String commute_ig;
	@Column(name = "commute_eg")
	private String commute_eg;
	@Column(name = "commute_yg")
	private String commute_yg;
	@Column(name = "commute_jg")
	private String commute_jg;
	@Column(name = "commute_hg")
	private String commute_hg;
	@Column(name = "commute_ru")
	private String commute_ru;
	@Column(name = "commute_rd")
	private Timestamp commute_rd;
	@Column(name = "commute_uu")
	private String commute_uu;
	@Column(name = "commute_ud")
	private Timestamp commute_ud;


	
	public static Commute setCommute(CommuteDTO commute) {
	    return Commute.builder()
	    		.employee_cd(commute.getEmployee_cd())
	    		.workinghour_id(commute.getWorkinghour_id())
	    		.commute_wd(commute.getCommute_wd())
	    		.commute_ld(commute.getCommute_ld())
	    		.commute_lt(commute.getCommute_lt())
	    		.commute_wt(commute.getCommute_wt())
	    		.commute_lt(commute.getCommute_lt())
	    		.commute_ig(commute.getCommute_ig())
	    		.commute_eg(commute.getCommute_eg())
	    		.commute_yg(commute.getCommute_yg())
	    		.commute_jg(commute.getCommute_jg())
	    		.commute_hg(commute.getCommute_hg())
	    		.commute_ru(commute.getCommute_ru())
	    		.commute_rd(commute.getCommute_rd())
	    		.commute_uu(commute.getCommute_uu())
	    		.commute_ud(commute.getCommute_ud())
	            .build();
	}

	
	
	
}
