package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
	private int stock_aq;
	@Column(name="stock_mq", precision = 19, scale = 0)
	private int stock_mq;
	@Column(name="stock_tp")
	private String stock_tp;
	@Column(name="stock_ru", length = 50)
	private String stock_ru;
	@Column(name="stock_rd")
	private Timestamp stock_rd;
	@Column(name="stock_uu", length = 50)
	private String stock_uu;
	@Column(name="stock_ud")
	private Timestamp stock_ud;
	
	
	public static Stock setStock(StockDTO stock) {
	    return Stock.builder()
	    		.item_cd(stock.getItem_cd())
	    		.warehouse_cd(stock.getWarehouse_cd())
	    		.warearea_cd(stock.getWarearea_cd())
	    		.stock_aq(stock.getStock_aq())
	    		.stock_tp(stock.getStock_tp())
	    		.stock_ru(stock.getStock_ru())
	    		.stock_rd(stock.getStock_rd())
	    		.stock_uu(stock.getStock_uu())
	    		.stock_ud(stock.getStock_ud())
	            .build();
	}
	
}
