package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "PRODUCT")
@NamedStoredProcedureQuery(
	    name = "UPSERT_PRODUCT_BATCH",
	    procedureName = "UPSERT_PRODUCT_BATCH",
	    parameters = {
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_cd", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_nm", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_gc", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_cc", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_gd", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_un", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_pr", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_wh", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_pc", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_rm", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_product_us", type = Integer.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_reg_user", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_size_list", type = String.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_color_list", type = String.class)
	    }
	)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ProductPK.class) // 복합 키 설정
@DynamicInsert
@DynamicUpdate
public class Product {
	
	@Id
	@Column(name = "product_cd", length = 30)
	private String product_cd;
	@Id
	@Column(name = "product_cc", length = 30)
	private String product_cc;
	@Id
	@Column(name = "product_sz", length = 200)
	private String product_sz;
	@Id
	@Column(name = "product_cr", length = 300)
	private String product_cr;
	@Column(name = "product_gc", length = 30)
	private String product_gc;
	@Column(name = "product_nm", length = 500)
	private String product_nm;
	@Column(name = "product_gd", length = 10)
	private String product_gd;
	@Column(name = "product_un", length = 10)
	private String product_un;
	@Column(name = "product_wh", length = 30)
	private String product_wh;
	@Column(name = "product_pr", precision = 19, scale = 0)
	private int product_pr;
	@Column(name = "product_pc", length = 255)
	private String product_pc;
	@Column(name = "product_rm", length = 1000)
	private String product_rm;
	@Column(name = "product_us")
	private boolean product_us;
	@Column(name = "product_ru", length = 50)
	private String product_ru;
	@Column(name = "product_rd")
	private Timestamp product_rd;
	@Column(name = "product_uu", length = 50)
	private String product_uu;
	@Column(name = "product_ud")
	private Timestamp product_ud;
	@Column(name = "product_fd", length = 50)
	private String product_fd;
	

	
	public static Product setProduct(ProductDTO product) {
	    return Product.builder()
	    		.product_cd(product.getProduct_cd())
	    		.product_gc(product.getProduct_gc())
	    		.product_cc(product.getProduct_cc())
	    		.product_nm(product.getProduct_nm())
	    		.product_sz(product.getProduct_sz())
	    		.product_cr(product.getProduct_cr())
	    		.product_gd(product.getProduct_gd())
	    		.product_un(product.getProduct_un())
	    		.product_wh(product.getProduct_wh())
	    		.product_pr(product.getProduct_pr())
	    		.product_pc(product.getProduct_pc())
	    		.product_rm(product.getProduct_rm())
	    		.product_us(product.isProduct_us())
	    		.product_ru(product.getProduct_ru())
	    		.product_rd(product.getProduct_rd())
	    		.product_uu(product.getProduct_uu())
	    		.product_ud(product.getProduct_ud())
	            .build();
	}

	
	
	
}
