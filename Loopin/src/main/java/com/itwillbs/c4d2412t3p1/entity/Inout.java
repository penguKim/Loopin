package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "INOUT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(InoutPK.class) // 복합 키 설정
@DynamicInsert
@DynamicUpdate
public class Inout {

	@Id
	@Column(name = "inout_dt", length = 30)
	private String inout_dt;
	@Id
	@Column(name="inout_iw", length = 30)
	private String inout_iw;
	@Id
	@Column(name="inout_ow", length = 30)
	private String inout_ow;
	@Id
	@Column(name="item_cd", length = 30)
	private String item_cd;
	@Id
	@Column(name="inout_io", length = 1)
	private String inout_io;
	@Column(name="inout_ia", length = 30)
	private String inout_ia;
	@Column(name="inout_oa", length = 30)
	private String inout_oa;
	@Column(name="inout_co", length = 30)
	private String inout_co;
	@Column(name="inout_tp", length = 1)
	private String inout_tp;
	@Column(name="lot_cd", length = 255)
	private String lot_cd;
	@Column(name="process_cd", length = 30)
	private String process_cd;
	@Column(name="inout_nn", precision = 19, scale = 0)
	private Integer inout_nn;
	@Column(name="inout_in", precision = 19, scale = 0)
	private Integer inout_in;
	@Column(name="inout_fn", precision = 19, scale = 0)
	private Integer inout_fn;
	@Column(name="employee_cd", length = 30)
	private String employee_cd;
	@Column(name="inout_ru", length = 50)
	private String inout_ru;
	@Column(name="inout_rd")
	private Timestamp inout_rd;
	@Column(name="inout_uu", length = 50)
	private String inout_uu;
	@Column(name="inout_ud")
	private Timestamp inout_ud;
	
	
	public static Inout setInout(InoutDTO inout) {
	    return Inout.builder()
	    		.inout_dt(inout.getInout_dt())
	    		.inout_iw(inout.getInout_iw())
	    		.inout_ow(inout.getInout_ow())
	    		.item_cd(inout.getItem_cd())
	    		.inout_ia(inout.getInout_ia())
	    		.inout_oa(inout.getInout_oa())
	    		.inout_co(inout.getInout_co())
	    		.inout_tp(inout.getInout_tp())
	    		.lot_cd(inout.getLot_cd())
	    		.process_cd(inout.getProcess_cd())
	    		.inout_nn(inout.getInout_nn())
	    		.inout_in(inout.getInout_in())
	    		.inout_fn(inout.getInout_fn())
	    		.inout_io(inout.getInout_io())
	    		.employee_cd(inout.getEmployee_cd())
	    		.inout_ru(inout.getInout_ru())
	    		.inout_rd(inout.getInout_rd())
	    		.inout_uu(inout.getInout_uu())
	    		.inout_ud(inout.getInout_ud())
	            .build();
	}
	
}
