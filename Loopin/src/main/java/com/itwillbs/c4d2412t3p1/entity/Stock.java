package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STOCK")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StockPK.class) // 복합 키 설정
@DynamicInsert
@DynamicUpdate
public class Stock {

	@Id
	@Column(name = "item_cd", length = 30)
	private String item_cd;
	@Id
	@Column(name="warehouse_cd", length = 30)
	private String warehouse_cd;
	@Id
	@Column(name="warearea_cd", length = 30)
	private String warearea_cd;
	@Column(name="stock_aq", precision = 19, scale = 0)
	private BigDecimal stock_aq;
	@Column(name="stock_mq", precision = 19, scale = 0)
	private BigDecimal stock_mq;
	@Column(name="stock_ru", length = 50)
	private String stock_ru;
	@Column(name="stock_rd")
	private Timestamp stock_rd;
	@Column(name="stock_uu", length = 50)
	private String stock_uu;
	@Column(name="stock_ud")
	private Timestamp stock_ud;
	
}
