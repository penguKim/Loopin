package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Query;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "COMMON_CODE")
@SequenceGenerator(
		name = "COMMON_SEQ_GEN"
	    , sequenceName = "COMMON_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
@Getter
@Setter
@ToString
public class Common_code {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMON_SEQ_GEN")
	@Column(name = "common_cd")
	private String common_cd;
	@Column(name = "common_gc")
	private String common_gc;
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

	
	
	
    // PrePersist 콜백 메서드로 기본 키 생성
    @PrePersist
    public void prePersist() {
        if (this.common_cd == null) { // 값이 없는 경우에만 설정
            this.common_cd = generateCommonCd(this.common_gc); // common_gc 값을 전달
        }
    }

    // 시퀀스를 사용해 '문자열-시퀀스' 형식으로 생성
    private String generateCommonCd(String commonGc) {
        if (commonGc == null || commonGc.isEmpty()) {
            throw new IllegalArgumentException("common_gc 값이 필요합니다.");
        }

        // 시퀀스 값 가져오기 (예: COMMON_SEQ.NEXTVAL)
        Long sequenceValue = getNextSequenceValue();

        // 원하는 형식으로 변환 ('common_gc-001', 'common_gc-002', ...)
        return commonGc + "-" + String.format("%03d", sequenceValue);
    }

    // 시퀀스 값 가져오기 (Native Query 사용)
    private Long getNextSequenceValue() {
        EntityManager em = Persistence.createEntityManagerFactory("default").createEntityManager();
        Query query = em.createNativeQuery("SELECT COMMON_SEQ.NEXTVAL FROM DUAL");
        return ((Number) query.getSingleResult()).longValue();
    }

	public static Common_code Common_code(Common_codeDTO common) {
		Common_code code = new Common_code();
		code.setCommon_gc(common.getCommon_gc());
		code.setCommon_cc(common.getCommon_cc());
		code.setCommon_nm(common.getCommon_nm());
		code.setCommon_ct(common.getCommon_ct());
		code.setCommon_in(common.getCommon_in());
		code.setCommon_us(common.getCommon_us());
		code.setCommon_ru(common.getCommon_ru());
		code.setCommon_rd(common.getCommon_rd());
		code.setCommon_uu(common.getCommon_uu());
		code.setCommon_ud(common.getCommon_ud());
		return code;
	}
	
}
