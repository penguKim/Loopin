package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.LotDTO;
import com.itwillbs.c4d2412t3p1.domain.LotHistoryDTO;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "LOTHISTORY")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
public class LotHistory {
	
	@EmbeddedId
    private LotHistoryPK id;  // ✅ 복합 키 적용

    @Column(name = "lothistory_st")
    private Timestamp lothistory_st;

    @Column(name = "lothistory_en")
    private Timestamp lothistory_en;

    @Column(name = "lothistory_wq")
    private long lothistory_wq;

    @Column(name = "lothistory_bq")
    private long lothistory_bq;

    public static LotHistory setLotHistory(LotHistoryDTO lotHistory) {
        return LotHistory.builder()
                .id(new LotHistoryPK(lotHistory.getContract_cd(), lotHistory.getLot_cd(), lotHistory.getProcess_cd()))  // ✅ 복합 키 객체 사용
                .lothistory_st(lotHistory.getLothistory_st())
                .lothistory_en(lotHistory.getLothistory_en())
                .lothistory_wq(lotHistory.getLothistory_wq())
                .lothistory_bq(lotHistory.getLothistory_bq())
                .build();
    }
	
}
