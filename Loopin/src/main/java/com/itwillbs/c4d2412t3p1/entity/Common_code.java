package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;

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



@Entity(name = "COMMON_CODE")
@Table(name = "COMMON_CODE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(common_codePK.class) // 복합 키 설정
public class Common_code {
	
//	@EmbeddedId
//	private common_codePK common_codePK;
	@Id
	@Column(name = "common_gc")
	private String common_gc;
	@Id
	@Column(name = "common_cc")
	private String common_cc;
	@Column(name = "common_nm")
	private String common_nm;
	@Column(name = "common_ct")
	private String common_ct;
	@Column(name = "common_in")
	private String common_in;
	@Column(name = "common_us")
	private String common_us;
	@Column(name = "common_ru")
	private String common_ru;
	@Column(name = "common_rd")
	private Timestamp common_rd;
	@Column(name = "common_uu")
	private String common_uu;
	@Column(name = "common_ud")
	private Timestamp common_ud;

//	public static Common_code Common_code(Common_codeDTO common) {
//		Common_code code = new Common_code();
//		common_codePK pk = new common_codePK();
//		pk.setCommon_cc(common.getCommon_cc());
//		pk.setCommon_gc(common.getCommon_gc());
//		code.setCommon_codePK(null);
//		code.setCommon_nm(common.getCommon_nm());
//		code.setCommon_ct(common.getCommon_ct());
//		code.setCommon_in(common.getCommon_in());
//		code.setCommon_us(common.getCommon_us());
//		code.setCommon_ru(common.getCommon_ru());
//		code.setCommon_rd(common.getCommon_rd());
//		code.setCommon_uu(common.getCommon_uu());
//		code.setCommon_ud(common.getCommon_ud());
//		return code;
//	}
	
	public static Common_code fromDTO(Common_codeDTO common) {
	    return Common_code.builder()
	    		.common_gc(common.getCommon_gc())
	    		.common_cc(common.getCommon_cc())
	            .common_nm(common.getCommon_nm())
	            .common_ct(common.getCommon_ct())
	            .common_in(common.getCommon_in())
	            .common_us(common.getCommon_us())
	            .common_ru(common.getCommon_ru())
	            .common_rd(common.getCommon_rd())
	            .common_uu(common.getCommon_uu())
	            .common_ud(common.getCommon_ud())
	            .build();
	}

	
	
	
}
