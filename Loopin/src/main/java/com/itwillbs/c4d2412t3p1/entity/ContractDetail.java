package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;

import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;

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
@Table(name = "CONTRACTDETAIL")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ContractDetalPK.class)
public class ContractDetail {

    @Id
    @Column(name = "contract_cd", length = 20)
    private String contract_cd;

    @Id
    @Column(name = "product_cd", length = 20)
    private String product_cd;

    @Id
    @Column(name = "product_sz", length = 20)
    private String product_sz;

    @Id
    @Column(name = "product_cr", length = 20)
    private String product_cr;

    @Column(name = "product_am")
    private Long product_am;

    @Column(name = "contract_ct")
    private BigDecimal contract_ct;

    @Column(name = "contract_ed")
    private String contract_ed;
    
    @Column(name = "product_un")
    private String product_un;

	
	public static ContractDetail setContractDetailEntity(ContractDetail contractDetail, ContractDetailDTO contractDetailDto) {
		
		contractDetail.setContract_cd(contractDetailDto.getContract_cd());
		contractDetail.setProduct_cd(contractDetailDto.getProduct_cd());
		contractDetail.setProduct_sz(contractDetailDto.getProduct_sz());
		contractDetail.setProduct_cr(contractDetailDto.getProduct_cr());
		contractDetail.setProduct_am(contractDetailDto.getProduct_am());
		contractDetail.setContract_ct(contractDetailDto.getContract_ct());
		contractDetail.setContract_ed(contractDetailDto.getContract_ed());
		contractDetail.setProduct_un(contractDetailDto.getProduct_un());
		
		return contractDetail;
    }
	
	
}
