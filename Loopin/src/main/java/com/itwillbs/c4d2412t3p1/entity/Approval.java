package com.itwillbs.c4d2412t3p1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;

@Entity
@Table(name = "APPROVAL")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "ap_sequence_generator", sequenceName = "ap_sequence", allocationSize = 1)
public class Approval {

	@Id
	@Column(name = "approval_cd", length = 15)
	private String approval_cd; // 결재코드

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
	private String approval_sd; // 결재 시작일

	@Column(name = "approval_ed")
	private String approval_ed; // 결재 종료일

	@Column(name = "approval_dv")
	private String approval_dv; // 구분(결재 유형)

	@Column(name = "approval_tt")
	private String approval_tt; // 제목

	@Lob
	@Column(name = "approval_ct", columnDefinition = "CLOB")
	private String approval_ct; // 내용

	@Column(name = "approval_fa", length = 20)
    private String approval_fa; // 1차 결재권자 (employee_cd)

    @Column(name = "approval_sa", length = 20)
    private String approval_sa; // 2차 결재권자 (employee_cd)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_fa", referencedColumnName = "employee_cd", insertable = false, updatable = false)
    private Employee firstApprover; // 1차 결재권자 (Employee 엔티티와 연관)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_sa", referencedColumnName = "employee_cd", insertable = false, updatable = false)
    private Employee secondApprover; // 2차 결재권자 (Employee 엔티티와 연관)

    @Column(name = "approval_av")
    private String approval_av; // 결재구분
    
	@Column(name = "approval_wr")
	private String approval_wr; // 작성자

	@Column(name = "approval_wd")
	private Timestamp approval_wd; // 작성일

	@Column(name = "approval_mf")
	private String approval_mf; // 수정자

	@Column(name = "approval_md")
	private Timestamp approval_md; // 수정일

	// 기본 생성자
	public Approval() {
	}

	// 매개변수 있는 생성자
	public Approval(String approval_cd, String approval_sd, String approval_ed, String approval_dv, String approval_tt,
	        String approval_ct, String approval_fa, String approval_sa, String approval_av, String approval_wr, Timestamp approval_wd,
	        String approval_mf, Timestamp approval_md, Long sequenceValue // 추가된 매개변수
	) {
	    this.approval_cd = approval_cd;
	    this.approval_sd = approval_sd;
	    this.approval_ed = approval_ed;
	    this.approval_dv = approval_dv;
	    this.approval_tt = approval_tt;
	    this.approval_ct = approval_ct;
	    this.approval_fa = approval_fa;
	    this.approval_sa = approval_sa;
	    this.approval_av = approval_av;
	    this.approval_wr = approval_wr;
	    this.approval_wd = approval_wd;
	    this.approval_mf = approval_mf;
	    this.approval_md = approval_md;
	    this.sequenceValue = sequenceValue;
	}

	// DTO를 기반으로 Approval 엔티티를 설정하는 메서드
	public static Approval setEmployeeEntity(Approval approval, ApprovalDTO approvalDto) {
	    ObjectMapper objectMapper = new ObjectMapper();

	    approval.setApproval_cd(approvalDto.getApproval_cd());
	    approval.setApproval_sd(approvalDto.getApproval_sd());
	    approval.setApproval_ed(approvalDto.getApproval_ed());
	    approval.setApproval_dv(approvalDto.getApproval_dv());
	    approval.setApproval_tt(approvalDto.getApproval_tt());

	    // Map을 JSON 문자열로 변환하여 저장
	    try {
	        approval.setApproval_ct(objectMapper.writeValueAsString(approvalDto.getApproval_ct()));
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Failed to serialize approval_ct", e);
	    }

	    approval.setApproval_fa(approvalDto.getApproval_fa());
	    approval.setApproval_sa(approvalDto.getApproval_sa());
	    approval.setApproval_av(approvalDto.getApproval_av());
	    approval.setApproval_wr(approvalDto.getApproval_wr());
	    approval.setApproval_wd(approvalDto.getApproval_wd());
	    approval.setApproval_mf(approvalDto.getApproval_mf());
	    approval.setApproval_md(approvalDto.getApproval_md());
	    return approval;
	}


	// ApprovalDTO와 시퀀스 값을 기반으로 Approval 엔티티 생성
	public static Approval createApproval(ApprovalDTO approvalDto, Long sequenceValue) {
	    ObjectMapper objectMapper = new ObjectMapper();

	    System.out.println("createApproval sequenceValue: " + sequenceValue);

	    String approvalCtJson;
	    try {
	        approvalCtJson = objectMapper.writeValueAsString(approvalDto.getApproval_ct());
	    } catch (JsonProcessingException e) {
	        throw new RuntimeException("Failed to serialize approval_ct", e);
	    }

	    return new Approval(null, // 결재코드는 prePersist로 자동 생성
	            approvalDto.getApproval_sd(), approvalDto.getApproval_ed(), approvalDto.getApproval_dv(),
	            approvalDto.getApproval_tt(), approvalCtJson, approvalDto.getApproval_fa(),
	            approvalDto.getApproval_sa(), approvalDto.getApproval_av(), approvalDto.getApproval_wr(), approvalDto.getApproval_wd(),
	            approvalDto.getApproval_mf(), approvalDto.getApproval_md(), sequenceValue);
	}

}
