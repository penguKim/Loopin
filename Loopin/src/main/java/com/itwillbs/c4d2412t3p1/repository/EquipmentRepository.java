package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {

	@Query(value = "SELECT LPAD(SEQ_EQUIPMENT_CD.NEXTVAL, 4, '0') FROM DUAL", nativeQuery = true)
    String nextEquipment_cd();
	
	@Query(value = "SELECT C.COMMON_CC, C.COMMON_NM FROM COMMON_CODE C WHERE COMMON_GC = 'HALFPRO' OR COMMON_GC = 'MATERIALS' OR COMMON_GC = 'SUBMAT' OR COMMON_GC = 'PRODUCT'", nativeQuery = true)
	List<Common_codeDTO> select_PRODUCT_list();
	
}
