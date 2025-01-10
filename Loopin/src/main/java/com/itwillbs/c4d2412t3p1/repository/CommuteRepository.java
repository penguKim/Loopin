package com.itwillbs.c4d2412t3p1.repository;

import java.time.LocalDate;
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

    @Query("SELECT c FROM Commute c WHERE c.commute_wd = :commute_wd")
    List<Commute> select_COMMUTE_detail(@Param("commute_wd") String commute_wd);
    
    @Query(value="SELECT commute_wd, COUNT(*) total FROM COMMUTE GROUP BY commute_wd", nativeQuery = true)
    List<CommuteDTO> select_COMMUTE_list();

    // 40시간 조회하기
    @Query("SELECT c FROM Commute c WHERE c.commute_wd BETWEEN :startDate AND :endDate")
    List<Commute> findCommutesBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
//    @Query(value = "SELECT c.commute_dt AS commuteDt, COUNT(*) AS total " +
//            "FROM COMMUTE c " +
//            "GROUP BY c.commute_dt", nativeQuery = true)
//List<CommuteDTO> select_COMMUTE_list();


}
