package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.LotDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "LOT")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class Lot {
	
	@EmbeddedId
    private LotPK id;  // ✅ `LotPK` 타입으로 설정 (복합 키 적용)

    @Column(name = "product_cd", length = 255)
    private String product_cd;
    
    @Column(name = "contract_cd", length = 15)
    private String contract_cd;
    
    @Column(name = "product_cr", length = 30)
    private String product_cr;
    
    @Column(name = "product_sz", length = 30)
    private String product_sz;
    
    @Column(name = "dailyproductplan_js")
    private Integer dailyproductplan_js;
    
    @Column(name = "lot_cr")
    private Timestamp lot_cr;

    public static Lot setLot(LotDTO lot) {
        return Lot.builder()
            .id(new LotPK(lot.getLot_cd(), lot.getProcess_cd()))  // ✅ LotPK 객체 생성
            .product_cd(lot.getProduct_cd())
            .contract_cd(lot.getContract_cd())
            .product_cr(lot.getProduct_cr())
            .product_sz(lot.getProduct_sz())
            .dailyproductplan_js(lot.getProductplan_js())
            .lot_cr(lot.getLot_cr())
            .build();
    }
}
