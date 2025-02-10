package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;

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
@Table(name = "MATERIAL")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(MaterialPK.class) // 복합 키 설정
@DynamicInsert
@DynamicUpdate
public class Material {
	
	@Id
	@Column(name = "material_cd", length = 30)
	private String material_cd;
	@Id
	@Column(name = "material_cc", length = 30)
	private String material_cc;
	@Column(name = "material_gc", length = 30)
	private String material_gc;
	@Column(name = "material_nm", length = 500)
	private String material_nm;
	@Column(name = "material_un", length = 10)
	private String material_un;
	@Column(name = "material_wh", length = 30)
	private String material_wh;
	@Column(name = "material_pr", length = 20)
	private String material_pr;
	@Column(name = "material_pc", length = 255)
	private String material_pc;
	@Column(name = "material_rm", length = 1000)
	private String material_rm;
	@Column(name = "material_us")
	private boolean material_us;
	@Column(name = "material_ru", length = 50)
	private String material_ru;
	@Column(name = "material_rd")
	private Timestamp material_rd;
	@Column(name = "material_uu", length = 50)
	private String material_uu;
	@Column(name = "material_ud")
	private Timestamp material_ud;


	
	public static Material setMaterial(MaterialDTO material) {
	    return Material.builder()
	    		.material_cd(material.getMaterial_cd())
	    		.material_cc(material.getMaterial_cc())
	    		.material_gc(material.getMaterial_gc())
	    		.material_nm(material.getMaterial_nm())
	    		.material_un(material.getMaterial_un())
	    		.material_wh(material.getMaterial_wh())
	    		.material_pr(material.getMaterial_pr())
	    		.material_pc(material.getMaterial_pc())
	    		.material_rm(material.getMaterial_rm())
	    		.material_us(material.isMaterial_us())
	    		.material_ru(material.getMaterial_ru())
	    		.material_rd(material.getMaterial_rd())
	    		.material_uu(material.getMaterial_uu())
	    		.material_ud(material.getMaterial_ud())
	            .build();
	}

	
	
	
}
