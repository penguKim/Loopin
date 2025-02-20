package com.itwillbs.c4d2412t3p1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCTPLANPROCESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Productplanprocess {
	
	@EmbeddedId
	private ProductplanprocessPK id;

	@Column(name = "PROCESS_CD", length = 10)
	private String process_cd;
}
