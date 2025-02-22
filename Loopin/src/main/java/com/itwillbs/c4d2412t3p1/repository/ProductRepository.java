package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
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
    
    @Query("SELECT p.product_cd FROM Product p WHERE p.product_cd = :productCd")
    List<Product> findByProductCd(@Param("productCd") String product_cd);

    @Query("SELECT p.product_cd, p.product_pc FROM Product p WHERE p.product_cd= :productCd GROUP BY p.product_cd, p.product_pc")
    Object[] findProductPcByproductCd(@Param("productCd") String product_cd);
    
    @Procedure(name = "UPSERT_PRODUCT_BATCH")
    void upsertProductBatch(
        @Param("p_product_cd") String productCd,
        @Param("p_product_nm") String productNm,
        @Param("p_product_gc") String productGc,
        @Param("p_product_cc") String productCc,
        @Param("p_product_gd") String productGd,
        @Param("p_product_un") String productUn,
        @Param("p_product_pr") String productPr,
        @Param("p_product_wh") String productWh,
        @Param("p_product_pc") String productPc,
        @Param("p_product_rm") String productRm,
        @Param("p_product_us") Integer productUs,
        @Param("p_reg_user") String regUser,
        @Param("p_size_list") String sizeList,
        @Param("p_color_list") String colorList
    );

}
