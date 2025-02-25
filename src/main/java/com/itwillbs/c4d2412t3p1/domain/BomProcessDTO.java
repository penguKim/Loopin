package com.itwillbs.c4d2412t3p1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BomProcessDTO {
	private String product_cd;
    private String process_cd;
    private String bomprocess_cd;
    private String bomprocess_ap;
    private String bomprocess_rt;
    
    public BomProcessDTO(String process_cd, String bomprocess_ap) {
        this.process_cd = process_cd;
        this.bomprocess_ap = bomprocess_ap;
    }
}
