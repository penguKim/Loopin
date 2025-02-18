package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itwillbs.c4d2412t3p1.domain.OrderDTO;

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
@Table(name = "ORDERS")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "od_sequence_generator", sequenceName = "od_sequence", allocationSize = 1)
public class Order {
	// 발주번호
    @Id
    @Column(name = "order_cd", length = 15)  // String 타입으로 저장, 길이 조정 가능
    private String order_cd;
	
	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드
    
	@PrePersist
	public void prePersist() {
	    if (this.order_cd == null && this.sequenceValue != null) {
	        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
	        String formattedSequence = String.format("%04d", this.sequenceValue);
	        this.order_cd = "OD" + currentYear + "-" + formattedSequence;
	    }
	}
    
	// 거래처코드
	@Column(name = "account_cd", length = 15)
	private String account_cd;
	
	// 사원코드
    @Column(name = "employee_cd", length = 15)
    private String employee_cd;
	
	// 담당자명
	@Column(name = "order_ps", length = 20)
	private String order_ps;

	// 발주일자
	@Column(name = "order_sd", length = 10)
	private String order_sd;

	// 입고일자
	@Column(name = "order_ed", length = 20)
	private String order_ed;
	
	// 발주수량
	@Column(name = "order_am")
	private long order_am;

	// 발주금액
	@Column(name = "order_mn")
	private BigDecimal order_mn;
	
	// 발주상태
	@Column(name = "order_st", length = 20)
	private String order_st;
	
	// 비고
	@Column(name = "order_rm")
	private String order_rm;
	
    // 작성자
    @Column(name = "order_wr")
    private String order_wr;
    
    // 작성일
    @Column(name = "order_wd")
    private Timestamp order_wd;

    // 수정자
    @Column(name = "order_mf")
    private String order_mf;

    // 수정일
    @Column(name = "order_md")
    private Timestamp order_md;
	
    
	// 생성자
	public Order() {}
	
	public Order(
			String order_cd,
			String account_cd,
			String employee_cd,
			String order_ps,
			String order_sd,
			String order_ed,
			long order_am,
			BigDecimal order_mn,
			String order_st,
			String order_rm,
			String order_wr,
			Timestamp order_wd,
			String order_mf,
			Timestamp order_md,
			Long sequenceValue
	) {
		this.order_cd = order_cd;
		this.account_cd = account_cd;
		this.employee_cd = employee_cd;
		this.order_ps = order_ps;
		this.order_sd = order_sd;
		this.order_ed = order_ed;
		this.order_am = order_am;
		this.order_mn = order_mn;
		this.order_st = order_st;
		this.order_rm = order_rm;
		this.order_wr = order_wr;
		this.order_wd = order_wd;
		this.order_mf = order_mf;
		this.order_md = order_md;
		this.sequenceValue = sequenceValue;
	}
	
	public static Order setOrderEntity(Order order, OrderDTO orderDto) {
		order.setOrder_cd(orderDto.getOrder_cd());
		order.setAccount_cd(orderDto.getAccount_cd());
		order.setEmployee_cd(orderDto.getEmployee_cd());
		order.setOrder_ps(orderDto.getOrder_ps());
		order.setOrder_sd(orderDto.getOrder_sd());
		order.setOrder_ed(orderDto.getOrder_ed());
		order.setOrder_am(orderDto.getOrder_am());
		order.setOrder_mn(orderDto.getOrder_mn());
		order.setOrder_st(orderDto.getOrder_st());
		order.setOrder_rm(orderDto.getOrder_rm());
		order.setOrder_wr(orderDto.getOrder_wr());
		order.setOrder_wd(orderDto.getOrder_wd());
		order.setOrder_mf(orderDto.getOrder_mf());
		order.setOrder_md(orderDto.getOrder_md());

		return order;
    }
	
	public static Order createOrder(OrderDTO orderDto, Long sequenceValue) {
			System.out.println("createOrder sequenceValue:" + sequenceValue);

	        return new Order(
	            null,
	            orderDto.getAccount_cd(),
	            orderDto.getEmployee_cd(),
	            orderDto.getOrder_ps(),
	            orderDto.getOrder_sd(),
	            orderDto.getOrder_ed(),
	            orderDto.getOrder_am(),
	            orderDto.getOrder_mn(),
	            orderDto.getOrder_st(),
	            orderDto.getOrder_rm(),
	            orderDto.getOrder_wr(),
	            orderDto.getOrder_wd(),
	            orderDto.getOrder_mf(),
	            orderDto.getOrder_md(),
	            sequenceValue
	    );
	}
	
}
