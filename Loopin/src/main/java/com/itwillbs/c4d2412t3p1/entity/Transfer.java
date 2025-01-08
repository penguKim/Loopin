package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.c4d2412t3p1.domain.MemberDTO;
import com.itwillbs.c4d2412t3p1.domain.TransferDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TRANSFER")
@Getter
@Setter
@ToString
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_seq_generator")
    @SequenceGenerator(name = "transfer_seq_generator", sequenceName = "TRANSFER_SEQ", allocationSize = 1)
	@Column(name = "transfer_id", nullable = false)
	private long transfer_id;
	
	@Column(name = "employee_cd")
	private String employee_cd;
	@Column(name = "transfer_od")
	private String transfer_od;
	@Column(name = "transfer_og")
	private String transfer_og;
	@Column(name = "transfer_ad")
	private String transfer_ad;
	@Column(name = "transfer_ac")
	private String transfer_ac;
	@Column(name = "transfer_adp")
	private String transfer_adp;
	@Column(name = "transfer_ag")
	private String transfer_ag;
	@Column(name = "transfer_aw")
	private String transfer_aw;
	@Column(name = "transfer_wr")
	private String transfer_wr;
	@Column(name = "transfer_wd")
	private String transfer_wd;
	@Column(name = "transfer_mf")
	private String transfer_mf;
	@Column(name = "transfer_md")
	private String transfer_md;

	// 생성자
	public Transfer() {
	}

	public static Transfer setTransferEntity(TransferDTO transferDTO) {
		Transfer transfer = new Transfer();
		transfer.setTransfer_id(transferDTO.getTransfer_id());
		transfer.setEmployee_cd(transferDTO.getEmployee_cd());
		transfer.setTransfer_od(transferDTO.getTransfer_od());
		transfer.setTransfer_og(transferDTO.getTransfer_og());
		transfer.setTransfer_ad(transferDTO.getTransfer_ad());
		transfer.setTransfer_ac(transferDTO.getTransfer_ac());
		transfer.setTransfer_adp(transferDTO.getTransfer_adp());
		transfer.setTransfer_ag(transferDTO.getTransfer_ag());
		transfer.setTransfer_aw(transferDTO.getTransfer_aw());
		transfer.setTransfer_wr(transferDTO.getTransfer_wr());
		transfer.setTransfer_wd(transferDTO.getTransfer_wd());
		transfer.setTransfer_mf(transferDTO.getTransfer_mf());
		transfer.setTransfer_md(transferDTO.getTransfer_md());
		return transfer;
	}

	public Transfer(long transfer_id, String employee_cd, String transfer_od, String transfer_og, String transfer_ad,
			String transfer_ac, String transfer_adp, String transfer_ag, String transfer_aw, String transfer_wr,
			String transfer_wd, String transfer_mf, String transfer_md) {
		super();
		this.transfer_id = transfer_id;
		this.employee_cd = employee_cd;
		this.transfer_od = transfer_od;
		this.transfer_og = transfer_og;
		this.transfer_ad = transfer_ad;
		this.transfer_ac = transfer_ac;
		this.transfer_adp = transfer_adp;
		this.transfer_ag = transfer_ag;
		this.transfer_aw = transfer_aw;
		this.transfer_wr = transfer_wr;
		this.transfer_wd = transfer_wd;
		this.transfer_mf = transfer_mf;
		this.transfer_md = transfer_md;
	}

}
