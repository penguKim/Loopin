package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;

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
@Table(name = "EQUIPMENT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Equipment {
	
	@Id
	@Column(name = "equipment_cd", length = 30, unique = true, nullable = false)
	private String equipment_cd;
	@Column(name = "model_cd", length = 255)
	private String model_cd;
	@Column(name = "equipment_nm", length = 255)
	private String equipment_nm;
	@Column(name = "warehouse_cd", length = 30)
	private String warehouse_cd;
	@Column(name = "equipment_us")
	private boolean equipment_us;
	@Column(name = "product_cd", length = 30)
	private String product_cd;
	@Column(name = "equipment_rm", length = 1000)
	private String equipment_rm;
	@Column(name = "equipment_pc", length = 255)
	private String equipment_pc;
	@Column(name = "equipment_ru", length = 50)
	private String equipment_ru;
	@Column(name = "equipment_rd")
	private Timestamp equipment_rd;
	@Column(name = "equipment_uu", length = 50)
	private String equipment_uu;
	@Column(name = "equipment_ud")
	private Timestamp equipment_ud;


	
	public static Equipment setEquipment(EquipmentDTO equipment) {
	    return Equipment.builder()
	    		.equipment_cd(equipment.getEquipment_cd())
	    		.model_cd(equipment.getModel_cd())
	    		.equipment_nm(equipment.getEquipment_nm())
	    		.warehouse_cd(equipment.getWarehouse_cd())
	    		.equipment_us(equipment.isEquipment_us())
	    		.product_cd(equipment.getProduct_cd())
	    		.equipment_rm(equipment.getEquipment_rm())
	    		.equipment_pc(equipment.getEquipment_pc())
	    		.equipment_ru(equipment.getEquipment_ru())
	    		.equipment_rd(equipment.getEquipment_rd())
	    		.equipment_uu(equipment.getEquipment_uu())
	    		.equipment_ud(equipment.getEquipment_ud())
	            .build();
	}

	
	
	
}
