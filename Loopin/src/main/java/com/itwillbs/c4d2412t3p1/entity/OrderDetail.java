package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;

import com.itwillbs.c4d2412t3p1.domain.OrderDetailDTO;

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
import lombok.ToString;

@Entity
@Table(name = "ORDERDETAIL")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(OrderDetalPK.class)
public class OrderDetail {

    @Id
    @Column(name = "order_cd", length = 20)
    private String order_cd;

    @Id
    @Column(name = "material_cd", length = 20)
    private String material_cd;

    @Column(name = "material_am")
    private Long material_am;

    @Column(name = "order_ct")
    private Long order_ct;

    @Column(name = "order_ed")
    private String order_ed;
    
    @Column(name = "material_un")
    private String material_un;

	
	public static OrderDetail setOrderDetailEntity(OrderDetail orderDetail, OrderDetailDTO orderDetailDto) {
		
		orderDetail.setOrder_cd(orderDetailDto.getOrder_cd());
		orderDetail.setMaterial_cd(orderDetailDto.getMaterial_cd());
		orderDetail.setMaterial_am(orderDetailDto.getMaterial_am());
		orderDetail.setOrder_ct(orderDetailDto.getOrder_ct());
		orderDetail.setMaterial_un(orderDetailDto.getMaterial_un());
		orderDetail.setOrder_ed(orderDetailDto.getOrder_ed());
		
		return orderDetail;
    }
	
	
}
