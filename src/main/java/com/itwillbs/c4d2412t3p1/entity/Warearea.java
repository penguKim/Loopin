package com.itwillbs.c4d2412t3p1.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;

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
@Table(name = "WAREAREA")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(WareareaPK.class) // 복합 키 설정
@DynamicInsert
@DynamicUpdate
public class Warearea {
	
	@Id
	@Column(name = "warehouse_cd", length = 30)
	private String warehouse_cd;
	@Id
	@Column(name = "warearea_cd", length = 30)
	private String warearea_cd;
	@Column(name = "warearea_nm", length = 255)
	private String warearea_nm;
	@Column(name = "warearea_cp", length = 10)
	private String warearea_cp;
	@Column(name = "warearea_in", length = 5)
	private String warearea_in;
	@Column(name = "warearea_us")
	private boolean warearea_us;
	@Column(name = "warearea_rm", length = 1000)
	private String warearea_rm;
	
	public static Warearea setWarehouse(WareareaDTO warearea) {
	    return Warearea.builder()
	    		.warehouse_cd(warearea.getWarehouse_cd())
	    		.warearea_cd(warearea.getWarearea_cd())
	    		.warearea_nm(warearea.getWarearea_nm())
	    		.warearea_cp(warearea.getWarearea_cp())
	    		.warearea_in(warearea.getWarearea_in())
	    		.warearea_us(warearea.isWarearea_us())
	    		.warearea_rm(warearea.getWarearea_rm())
	            .build();
	}

	
	
	
}
