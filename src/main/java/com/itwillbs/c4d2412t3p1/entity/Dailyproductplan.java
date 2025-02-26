package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DAILYPRODUCTPLAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dailyproductplan {

	@EmbeddedId
	private DailyproductplanPK id;
    
    @Column(name = "DAILYPRODUCTPLAN_JS")
    private Integer dailyproductplan_js;
    
    @Column(name = "DAILYPRODUCTPLAN_WR", length = 255)
    private String dailyproductplan_wr;
    
    @Column(name = "DAILYPRODUCTPLAN_WD")
    private Timestamp dailyproductplan_wd;
    
    @Column(name = "DAILYPRODUCTPLAN_MF", length = 255)
    private String dailyproductplan_mf;
    
    @Column(name = "DAILYPRODUCTPLAN_MD")
    private Timestamp dailyproductplan_md;
}
