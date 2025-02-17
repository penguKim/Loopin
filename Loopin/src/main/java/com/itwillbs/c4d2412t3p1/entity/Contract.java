package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itwillbs.c4d2412t3p1.domain.AccountDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CONTRACT")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "ct_sequence_generator", sequenceName = "ct_sequence", allocationSize = 1)
public class Contract {
	// 수주번호
    @Id
    @Column(name = "contract_cd", length = 15)  // String 타입으로 저장, 길이 조정 가능
    private String contract_cd;
	
	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드
    
	@PrePersist
	public void prePersist() {
	    if (this.contract_cd == null && this.sequenceValue != null) {
	        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
	        String formattedSequence = String.format("%04d", this.sequenceValue);
	        this.contract_cd = "CT" + currentYear + "-" + formattedSequence;
	    }
	}
    
	// 거래처코드
	@Column(name = "account_cd", length = 15)
	private String account_cd;
	
	// 담당자명
	@Column(name = "contract_ps", length = 20)
	private String contract_ps;

	// 수주일자
	@Column(name = "contract_sd", length = 10)
	private String contract_sd;

	// 납기일자
	@Column(name = "contract_ed", length = 20)
	private String contract_ed;
	
	// 수주수량
	@Column(name = "contract_am")
	private long contract_am;

	// 수주금액
	@Column(name = "contract_mn")
	private BigDecimal contract_mn;
	
	// 수주상태
	@Column(name = "contract_st", length = 20)
	private String contract_st;
	
	// 비고
	@Column(name = "contract_rm")
	private String contract_rm;
	
    // 작성자
    @Column(name = "contract_wr")
    private String contract_wr;
    
    // 작성일
    @Column(name = "contract_wd")
    private Timestamp contract_wd;

    // 수정자
    @Column(name = "contract_mf")
    private String contract_mf;

    // 수정일
    @Column(name = "contract_md")
    private Timestamp contract_md;
	
    
	// 생성자
	public Contract() {}
	
	public Contract(
			String contract_cd,
			String account_cd,
			String contract_ps,
			String contract_sd,
			String contract_ed,
			long contract_am,
			BigDecimal contract_mn,
			String contract_st,
			String contract_rm,
			String contract_wr,
			Timestamp contract_wd,
			String contract_mf,
			Timestamp contract_md,
			Long sequenceValue
	) {
		this.contract_cd = contract_cd;
		this.account_cd = account_cd;
		this.contract_ps = contract_ps;
		this.contract_sd = contract_sd;
		this.contract_ed = contract_ed;
		this.contract_am = contract_am;
		this.contract_mn = contract_mn;
		this.contract_st = contract_st;
		this.contract_rm = contract_rm;
		this.contract_wr = contract_wr;
		this.contract_wd = contract_wd;
		this.contract_mf = contract_mf;
		this.contract_md = contract_md;
		this.sequenceValue = sequenceValue;
	}
	
	public static Contract setContractEntity(Contract contract, ContractDTO contractDto) {
		contract.setContract_cd(contractDto.getContract_cd());
		contract.setAccount_cd(contractDto.getAccount_cd());
		contract.setContract_ps(contractDto.getContract_ps());
		contract.setContract_sd(contractDto.getContract_sd());
		contract.setContract_ed(contractDto.getContract_ed());
		contract.setContract_am(contractDto.getContract_am());
		contract.setContract_mn(contractDto.getContract_mn());
		contract.setContract_st(contractDto.getContract_st());
		contract.setContract_rm(contractDto.getContract_rm());
		contract.setContract_wr(contractDto.getContract_wr());
		contract.setContract_wd(contractDto.getContract_wd());
		contract.setContract_mf(contractDto.getContract_mf());
		contract.setContract_md(contractDto.getContract_md());

		return contract;
    }
	
	public static Contract createContract(ContractDTO contractDto, Long sequenceValue) {
			System.out.println("createContract sequenceValue:" + sequenceValue);

	        return new Contract(
	            null,
	            contractDto.getAccount_cd(),
	            contractDto.getContract_ps(),
	            contractDto.getContract_sd(),
	            contractDto.getContract_ed(),
	            contractDto.getContract_am(),
	            contractDto.getContract_mn(),
	            contractDto.getContract_st(),
	            contractDto.getContract_rm(),
	            contractDto.getContract_wr(),
	            contractDto.getContract_wd(),
	            contractDto.getContract_mf(),
	            contractDto.getContract_md(),
	            sequenceValue
	    );
	}
	
}
