package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "APPROVAL")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "ap_sequence_generator", sequenceName = "ap_sequence", allocationSize = 1)
public class Approval {
	
    @Id
    @Column(name = "approval_cd", length = 15)  // String 타입으로 저장, 길이 조정 가능
    private String approval_cd;  // String으로 변경
    
	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드
    
	@PrePersist
	public void prePersist() {
	    if (this.approval_cd == null && this.sequenceValue != null) {
	        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
	        String formattedSequence = String.format("%04d", this.sequenceValue);
	        this.approval_cd = "ap" + currentYear + "-" + formattedSequence;
	    }
	}

    @Column(name = "approval_sd")
    private String approval_sd;

    @Column(name = "approval_ed")
    private String approval_ed;

    @Column(name = "approval_dv")
    private String approval_dv;

    @Column(name = "approval_tt")
    private String approval_tt;

    @Column(name = "approval_ct")
    private String approval_ct;

    @Column(name = "approval_wr")
    private String approval_wr;

    @Column(name = "approval_wd")
    private Timestamp approval_wd;

    @Column(name = "approval_mf")
    private String approval_mf;

    @Column(name = "approval_md")
    private Timestamp approval_md;
    
	
	// 생성자
	public Approval() {
	}
	
	// Employee 클래스에 새 생성자 추가
	public Approval(
			String approval_cd,
			String approval_sd,
			String approval_ed,
			String approval_dv,
			String approval_tt,
			String approval_ct,
			String approval_wr,
			Timestamp approval_wd,
			String approval_mf,
			Timestamp approval_md,	    
			Long sequenceValue // 추가된 매개변수
	) {
		this.approval_cd = approval_cd;
		this.approval_sd = approval_sd;
		this.approval_ed = approval_ed;
		this.approval_dv = approval_dv;
		this.approval_tt = approval_tt;
		this.approval_ct = approval_ct;
		this.approval_wr = approval_wr;
		this.approval_wd = approval_wd;
		this.approval_mf = approval_mf;
		this.approval_md = approval_md;
	    this.sequenceValue = sequenceValue; // 필드 설정
	}


    public static Approval setEmployeeEntity(Approval approval, ApprovalDTO approvalDto) {
    	approval.setApproval_cd(approvalDto.getApproval_cd());
    	approval.setApproval_sd(approvalDto.getApproval_sd());
    	approval.setApproval_ed(approvalDto.getApproval_ed());
    	approval.setApproval_dv(approvalDto.getApproval_dv());
    	approval.setApproval_tt(approvalDto.getApproval_tt());
    	approval.setApproval_ct(approvalDto.getApproval_ct());
    	approval.setApproval_wr(approvalDto.getApproval_wr());
    	approval.setApproval_wd(approvalDto.getApproval_wd());
    	approval.setApproval_mf(approvalDto.getApproval_mf());
    	approval.setApproval_md(approvalDto.getApproval_md());

        

        return approval;
    }
    
    public static Approval createApproval(ApprovalDTO approvalDto, Long sequenceValue) {
        System.out.println("createEmployee sequenceValue:" + sequenceValue);

        return new Approval(
            null,
            approvalDto.getApproval_sd(),
            approvalDto.getApproval_ed(),
            approvalDto.getApproval_dv(),
            approvalDto.getApproval_tt(),
            approvalDto.getApproval_ct(),
            approvalDto.getApproval_wr(),
            approvalDto.getApproval_wd(),
            approvalDto.getApproval_mf(),
            approvalDto.getApproval_md(),
            sequenceValue
        );
    }
    
}
	
