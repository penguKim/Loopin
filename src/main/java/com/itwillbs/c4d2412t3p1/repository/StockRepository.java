package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.entity.StockPK;


@Repository
public interface StockRepository extends JpaRepository<Stock, StockPK> {

	@Modifying
	@Query("""
			DELETE FROM Stock s
			 WHERE (s.item_cd IN :combinedList)
			   AND s.warehouse_cd = :warehouseCd
			   AND s.warearea_cd = :wareareaCd
	       """)
	void deleteByCombinedIds(@Param("combinedList") List<String> combinedList,
	                         @Param("warehouseCd") String warehouseCd,
	                         @Param("wareareaCd") String wareareaCd);
	
	
	@Query("SELECT s FROM Stock s " + 
		       "WHERE s.item_cd = :itemCd " +
		       "ORDER BY CASE WHEN s.warehouse_cd = :inoutIw AND s.warearea_cd = :inoutIa THEN 0 ELSE 1 END")
		List<Stock> findStocksForDeduction(@Param("itemCd") String itemCd,
		                                   @Param("inoutIw") String inoutIw,
		                                   @Param("inoutIa") String inoutIa);

}
