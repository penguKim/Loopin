package com.itwillbs.c4d2412t3p1.entity;

import com.itwillbs.c4d2412t3p1.domain.ComhistoryDTO;

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
@Table(name = "COMHISTORY")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ComhistoryPK.class) // 복합 키 설정
public class Comhistory {
	
	@Id
	@Column(name = "employee_cd")
	private String employee_cd;
	@Id
	@Column(name = "comhistory_ym")
	private String comhistory_ym;
	@Column(name = "comhistory_al")
	private String comhistory_al;
	@Column(name = "comhistory_ig")
	private String comhistory_ig;
	@Column(name = "comhistory_yg")
	private String comhistory_yg;
	@Column(name = "comhistory_hg")
	private String comhistory_hg;
	@Column(name = "comhistory_jg")
	private String comhistory_jg;
	@Column(name = "comhistory_eg")
	private String comhistory_eg;



	
	public static Comhistory setComhistory(ComhistoryDTO comhistory) {
	    return Comhistory.builder()
	    		.employee_cd(comhistory.getEmployee_cd())
	    		.comhistory_ym(comhistory.getComhistory_ym())
	    		.comhistory_al(comhistory.getComhistory_al())
	    		.comhistory_ig(comhistory.getComhistory_ig())
	    		.comhistory_yg(comhistory.getComhistory_yg())
	    		.comhistory_hg(comhistory.getComhistory_hg())
	    		.comhistory_jg(comhistory.getComhistory_jg())
	    		.comhistory_eg(comhistory.getComhistory_eg())
	            .build();
	}

	
	
	
}
