package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
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
@Table(name = "PRODUCTPLAN")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Productplan {

	@EmbeddedId
	private ProductplanPK id;

	@Column(name = "PRODUCTPLAN_SD")
	private Timestamp productplan_sd;

	@Column(name = "PRODUCTPLAN_ED")
	private Timestamp productplan_ed;

	@Column(name = "PRODUCTPLAN_DD", length = 255)
	private String productplan_dd;

	@Column(name = "PRODUCTPLAN_JS")
	private Integer productplan_js;

	@Column(name = "WAREHOUSE_CD", length = 255)
	private String warehouse_cd;

	@Column(name = "PRODUCTPLAN_ST", length = 255)
	private String productplan_st;

	@Column(name = "PRODUCTPLAN_BG", length = 255)
	private String productplan_bg;

	@Column(name = "PRODUCTPLAN_WR", length = 255)
	private String productplan_wr;

	@Column(name = "PRODUCTPLAN_WD")
	private Timestamp productplan_wd;

	@Column(name = "PRODUCTPLAN_MF", length = 255)
	private String productplan_mf;

	@Column(name = "PRODUCTPLAN_MD")
	private Timestamp productplan_md;

}
