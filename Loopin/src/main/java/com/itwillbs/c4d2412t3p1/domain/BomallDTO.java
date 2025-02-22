package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class BomallDTO {

	    @JsonProperty("PRODUCT_CD")
	    private String product_cd;

	    @JsonProperty("PROCESS_CD")
	    private String process_cd;

	    @JsonProperty("BOMPROCESS_CD")
	    private String bomprocess_cd;

	    @JsonProperty("BOMPROCESS_AP")
	    private String bomprocess_ap;

	    @JsonProperty("BOMPROCESS_RT")
	    private String bomprocess_rt;

	    @JsonProperty("BOMPROCESS_ER")
	    private String bomprocess_er;

	    @JsonProperty("BOMPROCESS_WR")
	    private String bomprocess_wr;

	    @JsonProperty("BOMPROCESS_WD")
	    private Timestamp bomprocess_wd;

	    @JsonProperty("BOMPROCESS_MF")
	    private String bomprocess_mf;

	    @JsonProperty("BOMPROCESS_MD")
	    private Timestamp bomprocess_md;

	    @JsonProperty("BOMPROCESS_BG")
	    private String bomprocess_bg;

	    @JsonProperty("BOMPRODUCT_CD")
	    private String bomproduct_cd;

	    @JsonProperty("BOM_CD")
	    private String bom_cd;

	    @JsonProperty("BOM_AM")
	    private long bom_am;

}
