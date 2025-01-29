package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
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
@Table(name = "WAREHOUSE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Warehouse {
	
	@Id
	@Column(name = "warehouse_cd", length = 30)
	private String warehouse_cd;
	@Column(name = "warehouse_nm", length = 255)
	private String warehouse_nm;
	@Column(name = "warehouse_tp", length = 30)
	private String warehouse_tp;
	@Column(name = "warehouse_in", length = 10)
	private String warehouse_in;
	@Column(name = "warehouse_us")
	private boolean warehouse_us;
	@Column(name = "warehouse_rm", length = 1000)
	private String warehouse_rm;
	@Column(name = "warehouse_ru", length = 50)
	private String warehouse_ru;
	@Column(name = "warehouse_rd")
	private Timestamp warehouse_rd;
	@Column(name = "warehouse_uu", length = 50)
	private String warehouse_uu;
	@Column(name = "warehouse_ud")
	private Timestamp warehouse_ud;


	
	public static Warehouse setWarehouse(WarehouseDTO warehouse) {
	    return Warehouse.builder()
	    		.warehouse_cd(warehouse.getWarehouse_cd())
	    		.warehouse_nm(warehouse.getWarehouse_nm())
	    		.warehouse_tp(warehouse.getWarehouse_tp())
	    		.warehouse_in(warehouse.getWarehouse_in())
	    		.warehouse_us(warehouse.isWarehouse_us())
	    		.warehouse_rm(warehouse.getWarehouse_rm())
	    		.warehouse_ru(warehouse.getWarehouse_ru())
	    		.warehouse_rd(warehouse.getWarehouse_rd())
	    		.warehouse_uu(warehouse.getWarehouse_uu())
	    		.warehouse_ud(warehouse.getWarehouse_ud())
	            .build();
	}

	
	
	
}
