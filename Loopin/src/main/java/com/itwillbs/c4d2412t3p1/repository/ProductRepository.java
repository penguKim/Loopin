package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Product;
import com.itwillbs.c4d2412t3p1.entity.ProductPK;


@Repository
public interface ProductRepository extends JpaRepository<Product, ProductPK> {

	@Query("SELECT p FROM Product p WHERE p.product_cd = :productCd AND p.product_cc = :productCc")
	List<Product> findByProductCdAndItemCd(@Param("productCd") String productCd, @Param("productCc") String productCc);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.product_cd = :productCd AND p.product_cc = :productCc AND (p.product_sz NOT IN :sizes OR p.product_cr NOT IN :colors)")
    void deleteBySizeOrColor(@Param("productCd") String productCd, 
                            @Param("productCc") String productCc,
                            @Param("sizes") List<String> sizes,
                            @Param("colors") List<String> colors);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.product_cd IN :productCodes")
	void deleteByProductCdIn(@Param("productCodes") List<String> procudtCodes);
    
    @Query("SELECT p FROM Product p WHERE p.product_cd = :productCd")
    List<Product> findByProductCd(@Param("productCd") String product_cd);

    @Query("SELECT p.product_cd, p.product_pc FROM Product p WHERE p.product_cd= :productCd GROUP BY p.product_cd, p.product_pc")
    Object[] findProductPcByproductCd(@Param("productCd") String product_cd);

}
