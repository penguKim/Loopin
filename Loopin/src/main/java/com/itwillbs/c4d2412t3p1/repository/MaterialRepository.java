package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.entity.Material;
import com.itwillbs.c4d2412t3p1.entity.MaterialPK;
import com.itwillbs.c4d2412t3p1.entity.Product;

@Repository
public interface MaterialRepository extends JpaRepository<Material, MaterialPK> {

	@Modifying
	@Query("DELETE FROM Material m WHERE m.material_cd IN :materialCodes")
	void deleteByMaterialCd(@Param("materialCodes") List<String> materialCodes);

	@Query("SELECT m FROM Material m WHERE m.material_cd = :materialCd")
	List<Material> findByMaterialCd(@Param("materialCd") String material_cd);

//     자재코드로 자재 정보 조회 (엔티티 전체 반환)
    @Query("SELECT m FROM Material m WHERE m.material_cd = :materialCd")
    Material findMaterialEntityByCd(@Param("materialCd") String materialCd);

//     자재코드로 자재 구분(MATERIALS or SUBMAT)만 반환
    @Query("SELECT m.material_gc FROM Material m WHERE m.material_cd = :materialCd")
    String findMaterialGcByMaterialCd(@Param("materialCd") String materialCd);

}
