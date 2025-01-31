package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.AccountDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "ac_sequence_generator", sequenceName = "ac_sequence", allocationSize = 1)
public class Account {
	
	// 거래처 코드
    @Id
    @Column(name = "account_cd", length = 15)  // String 타입으로 저장, 길이 조정 가능
    private String account_cd;  // String으로 변경
    
	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드
    
	@PrePersist
	public void prePersist() {
	    if (this.account_cd == null && this.sequenceValue != null) {
	        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
	        String formattedSequence = String.format("%04d", this.sequenceValue);
	        this.account_cd = "AC" + currentYear + "-" + formattedSequence;
	    }
	}
	

	// 거래처명
	@Column(name = "account_nm", length = 20)
	private String account_nm;

	// 거래처 구분
	@Column(name = "account_dv", length = 20)
	private String account_dv;
	
	// 대표자명
	@Column(name = "account_cp", length = 20)
	private String account_cp;

	// 담당자명
	@Column(name = "account_ps", length = 20)
	private String account_ps;

	// 연락처
	@Column(name = "account_ph", length = 20)
	private String account_ph;
	
	// 팩스번호
	@Column(name = "account_fx", length = 20)
	private String account_fx;

	// 이메일
	@Column(name = "account_em", length = 20)
	private String account_em;

	// 사업자번호
	@Column(name = "account_sb", length = 20)
	private String account_sb;
	
	// 업종
	@Column(name = "account_uj", length = 20)
	private String account_uj;

	// 업태
	@Column(name = "account_ut", length = 20)
	private String account_ut;

	// 주소
	@Column(name = "account_ad", length = 100)
	private String account_ad;

	// 거래시작일
	@Column(name = "account_sd", length = 10)
    private String account_sd;

	// 거래종료일
    @Column(name = "account_ed", length = 10)
    private String account_ed;
    
    // 은행명
    @Column(name = "account_bk", length = 20)
    private String account_bk;
    
    // 계좌번호
    @Column(name = "account_an", length = 20)
    private String account_an;
    
    // 예금주
    @Column(name = "account_dt", length = 20)
    private String account_dt;
    
    // 사용여부
    @Column(name = "account_us", length = 10)
    private Boolean account_us;
    
    // 비고
    @Column(name = "account_nt", length = 200)
    private String account_nt;

    // 작성자
    @Column(name = "account_wr", length = 10)
    private String account_wr;
    
    // 작성일
    @Column(name = "account_wd")
    private Timestamp account_wd;

    // 수정자
    @Column(name = "account_mf", length = 10)
    private String account_mf;

    // 수정일
    @Column(name = "account_md")
    private Timestamp account_md;
    
	
	// 생성자
	public Account() {
	}
	
	// Account 클래스에 새 생성자 추가
	public Account(
			String account_cd,
			String account_nm,
			String account_dv,
			String account_cp,
			String account_ps,
			String account_ph,
			String account_fx,
			String account_em,
			String account_sb,
			String account_uj,
			String account_ut,
			String account_ad,
			String account_sd,
			String account_ed,
			String account_bk,
			String account_an,
			String account_dt,
			Boolean account_us,
			String account_nt,
			String account_wr,
			Timestamp account_wd,
			String account_mf,
			Timestamp account_md,	    
			Long sequenceValue // 추가된 매개변수
	) {
		this.account_cd = account_cd;
		this.account_nm = account_nm;
		this.account_dv = account_dv;
		this.account_cp = account_cp;
		this.account_ps = account_ps;
		this.account_ph = account_ph;
		this.account_fx = account_fx;
		this.account_em = account_em;
		this.account_sb = account_sb;
		this.account_uj = account_uj;
		this.account_ut = account_ut;
		this.account_ad = account_ad;
		this.account_sd = account_sd;
		this.account_ed = account_ed;
		this.account_bk = account_bk;
		this.account_an = account_an;
		this.account_dt = account_dt;
		this.account_us = account_us;
		this.account_nt = account_nt;
		this.account_wr = account_wr;
		this.account_wd = account_wd;
		this.account_mf = account_mf;
		this.account_md = account_md;
	    this.sequenceValue = sequenceValue; // 필드 설정
	}


    public static Account setAccountEntity(Account account, AccountDTO accountDto) {
    	account.setAccount_cd(accountDto.getAccount_cd());
    	account.setAccount_nm(accountDto.getAccount_nm());
    	account.setAccount_dv(accountDto.getAccount_dv());
    	account.setAccount_cp(accountDto.getAccount_cp());
    	account.setAccount_ps(accountDto.getAccount_ps());
    	account.setAccount_ph(accountDto.getAccount_ph());
    	account.setAccount_fx(accountDto.getAccount_fx());
    	account.setAccount_em(accountDto.getAccount_em());
    	account.setAccount_sb(accountDto.getAccount_sb());
    	account.setAccount_uj(accountDto.getAccount_uj());
    	account.setAccount_ut(accountDto.getAccount_ut());
    	account.setAccount_ad(accountDto.getAccount_ad());
    	account.setAccount_sd(accountDto.getAccount_sd());
    	account.setAccount_ed(accountDto.getAccount_ed());
    	account.setAccount_bk(accountDto.getAccount_bk());
    	account.setAccount_an(accountDto.getAccount_an());
    	account.setAccount_dt(accountDto.getAccount_dt());
    	account.setAccount_us(accountDto.getAccount_us());
    	account.setAccount_nt(accountDto.getAccount_nt());
    	account.setAccount_wr(accountDto.getAccount_wr());
    	account.setAccount_wd(accountDto.getAccount_wd());
    	account.setAccount_mf(accountDto.getAccount_mf());
    	account.setAccount_md(accountDto.getAccount_md());

        return account;
    }
    
    public static Account createAccount(AccountDTO accountDto, Long sequenceValue) {
        System.out.println("createAccount sequenceValue:" + sequenceValue);

        return new Account(
            null,  // account_cd는 @PrePersist에서 설정됨
            accountDto.getAccount_nm(),
            accountDto.getAccount_dv(),
            accountDto.getAccount_cp(),
            accountDto.getAccount_ps(),
            accountDto.getAccount_ph(),
            accountDto.getAccount_fx(),
            accountDto.getAccount_em(),
            accountDto.getAccount_sb(),
            accountDto.getAccount_uj(),
            accountDto.getAccount_ut(),
            accountDto.getAccount_ad(),
            accountDto.getAccount_sd(),
            accountDto.getAccount_ed(),
            accountDto.getAccount_bk(),
            accountDto.getAccount_an(),
            accountDto.getAccount_dt(),
            accountDto.getAccount_us(),
            accountDto.getAccount_nt(),
            accountDto.getAccount_wr(),
            accountDto.getAccount_wd(),
            accountDto.getAccount_mf(),
            accountDto.getAccount_md(),

            sequenceValue
        );
    }
    
}
	
