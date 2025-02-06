package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Material;
import com.itwillbs.c4d2412t3p1.entity.MaterialPK;


@Repository
public interface MaterialRepository extends JpaRepository<Material, MaterialPK> {

    @Modifying
    @Query("DELETE FROM Material m WHERE m.material_cd IN :materialCodes")
	void deleteByMaterialCd(@Param("materialCodes") List<String> materialCodes);

}
