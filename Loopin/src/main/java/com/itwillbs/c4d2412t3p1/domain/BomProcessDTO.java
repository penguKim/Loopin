package com.itwillbs.c4d2412t3p1.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BomProcessDTO {
	private String product_cd;
    private String process_cd;
    private String bomprocess_cd;
    private String bomprocess_ap;
    private String bomprocess_rt;
}
