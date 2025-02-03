package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itwillbs.c4d2412t3p1.domain.NoticeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "NOTICE")
@Getter
@Setter
@ToString(exclude = "employee")
@SequenceGenerator(name = "nt_sequence_generator", sequenceName = "nt_sequence", allocationSize = 1)
public class Notice {
	
    @Id
    @Column(name = "notice_cd", length = 15)  //  String 타입으로 저장, 길이 조정 가능
    private String notice_cd;  // String으로 변경

    
	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드
    
	@PrePersist
	public void prePersist() {
	    if (this.notice_cd == null && this.sequenceValue != null) {
	        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
	        String formattedSequence = String.format("%04d", this.sequenceValue);
	        this.notice_cd = "nt" + currentYear + "-" + formattedSequence;
	    }
	}

    @Column(name = "notice_tt")
    private String notice_tt;

    @Column(name = "notice_ct")
    private String notice_ct;

    @Column(name = "notice_wr")
    private String notice_wr;

    @Column(name = "notice_wd")
    private Timestamp notice_wd;

    @Column(name = "notice_mf")
    private String notice_mf;

    @Column(name = "notice_md")
    private Timestamp notice_md;
    

    
    
	// 생성자
	public Notice() {
	}
	
	public Notice(
		 	 String notice_cd,
			 String notice_tt,
			 String notice_ct,
//			 String notice_fl,
			 String notice_wr,
			 Timestamp notice_wd,
			 String notice_mf,
			 Timestamp notice_md,	    
			Long sequenceValue // 추가된 매개변수
	) {
		this.notice_cd = notice_cd;
		this.notice_tt = notice_tt;
		this.notice_ct = notice_ct;
//		this.notice_fl = notice_fl;
		this.notice_wr = notice_wr;
		this.notice_wd = notice_wd;
		this.notice_mf = notice_mf;
		this.notice_md = notice_md;
	    this.sequenceValue = sequenceValue; // 필드 설정
	}


    public static Notice setNoticeEntity(Notice notice, NoticeDTO noticeDTO) {
    	notice.setNotice_cd(noticeDTO.getNotice_cd());
    	notice.setNotice_tt(noticeDTO.getNotice_tt());
    	notice.setNotice_ct(noticeDTO.getNotice_ct());
    	notice.setNotice_wr(noticeDTO.getNotice_wr());
    	notice.setNotice_wd(noticeDTO.getNotice_wd());
    	notice.setNotice_mf(noticeDTO.getNotice_mf());
    	notice.setNotice_md(noticeDTO.getNotice_md());

        return notice;
    }
    
    public static Notice createNotice( NoticeDTO noticeDTO, Long sequenceValue) {
        System.out.println("createNotice sequenceValue:" + sequenceValue);

        return new Notice(
            null,
            noticeDTO.getNotice_tt(),
            noticeDTO.getNotice_ct(),
            noticeDTO.getNotice_wr(),
            noticeDTO.getNotice_wd(),
            noticeDTO.getNotice_mf(),
            noticeDTO.getNotice_md(),
            sequenceValue
        );
    }
    
}
	
