package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.entity.ContractDetalPK;

public interface ContractDetailRepository extends JpaRepository<ContractDetail, ContractDetalPK> {

	// 진행중 상태의 수주번호 리스트 조회
	@Query("""
			    SELECT d FROM ContractDetail d
			    JOIN Contract c ON d.contract_cd = c.contract_cd
			    WHERE c.contract_st = '진행중'
			    AND (:contractCd IS NULL OR d.contract_cd = :contractCd)
			""")
	List<ContractDetail> select_CONTRACTDETAIL_BY_STATUS_list(@Param("contractCd") String contractCd);

	// 같은 수주번호, 품목코드를 가진 모든 컬러·사이즈의 총합을 조회
	@Query("SELECT SUM(d.product_am) FROM ContractDetail d "
			+ "WHERE d.contract_cd = :contractCd AND d.product_cd = :productCd")
	Integer findTotalContractAmount(@Param("contractCd") String contractCd, @Param("productCd") String productCd);

//  특정 (수주번호, 품목코드)에 해당하는
//  모든 ContractDetail 레코드 조회
	@Query("SELECT cd FROM ContractDetail cd " + "WHERE cd.contract_cd = :contractCd "
			+ "  AND cd.product_cd  = :productCd")
	List<ContractDetail> findByContractCdAndProductCd(@Param("contractCd") String contractCd,
			@Param("productCd") String productCd);
}
