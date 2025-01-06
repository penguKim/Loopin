package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.CommutePK;


@Repository
public interface CommuteRepository extends JpaRepository<Commute, CommutePK> {

    @Query("SELECT c FROM Commute c WHERE c.commute_dt = :commute_dt")
    List<Commute> select_COMMUTE(@Param("commute_dt") String commute_dt);
    
    @Query(value="SELECT commute_dt, COUNT(*) total FROM COMMUTE GROUP BY commute_dt", nativeQuery = true)
    List<CommuteDTO> select_COMMUTE_list();
    
//    @Query(value = "SELECT c.commute_dt AS commuteDt, COUNT(*) AS total " +
//            "FROM COMMUTE c " +
//            "GROUP BY c.commute_dt", nativeQuery = true)
//List<CommuteDTO> select_COMMUTE_list();


}
