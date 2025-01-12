package com.itwillbs.c4d2412t3p1.entity;
import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@ToString
@Table(name = "PR")
public class PR {
	@Id
	@Column(name = "pr_id")
	private long pr_id;
	
	@Column(name="pr_gm")
	private String pr_gm;
	
	@Column(name="pr_wm")
	private String pr_wm;
	
	@Column(name="pr_td")
	private String pr_td;
	
	@Column(name="pr_ta")
	private String pr_ta;
	
	@Column(name="pr_wr")
	private String pr_wr;
	
	@Column(name="pr_wd")
	private String pr_wd;
}
