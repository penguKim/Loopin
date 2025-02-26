package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workorder {
	@Id
    @Column(name = "WORKORDER_CD", length = 255)
    private String workorder_cd;

    @Column(name = "LOT_CD", length = 255)
    private String lot_cd;

    @Column(name = "DAILYPRODUCTPLAN_SD")
    private Timestamp dailyproductplan_sd;

    @Column(name = "DAILYPRODUCTPLAN_JB", length = 255)
    private String dailyproductplan_jb;

    @Column(name = "WORKORDER_SD")
    private Timestamp workorder_sd;

    @Column(name = "WORKORDER_ED")
    private Timestamp workorder_ed;

    @Column(name = "WORKORDER_ST", length = 2)
    private String workorder_st;
    
    @Column(name = "PROCESS_CD")
    private String process_cd;

}
