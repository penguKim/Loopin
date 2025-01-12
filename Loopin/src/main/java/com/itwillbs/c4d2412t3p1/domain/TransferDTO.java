package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransferDTO {

	private long transfer_id;
    private String employee_cd;
    private String transfer_od;
    private String transfer_og;
    private String transfer_ad;
    private String transfer_ac;
    private String transfer_adp;
    private String transfer_ag;
    private Boolean transfer_aw;
    private String transfer_wr;
    private String transfer_wd;
    private String transfer_mf;
    private String transfer_md;
    private Boolean transfer_mg;
}
