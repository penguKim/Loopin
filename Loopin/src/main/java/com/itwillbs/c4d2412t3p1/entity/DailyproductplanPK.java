package com.itwillbs.c4d2412t3p1.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DailyproductplanPK implements Serializable {
    
    @Column(name = "DAILYPRODUCTPLAN_SD")
    private Timestamp dailyproductplan_sd;

    @Column(name = "CONTRACT_CD", length = 255)
    private String contract_cd;

    @Column(name = "PRODUCT_CD", length = 255)
    private String product_cd;

    @Column(name = "PROCESS_CD", length = 255)
    private String process_cd;

    @Column(name = "PRODUCT_CR", length = 255)
    private String product_cr;

    @Column(name = "PRODUCT_SZ", length = 255)
    private String product_sz;
    
}
