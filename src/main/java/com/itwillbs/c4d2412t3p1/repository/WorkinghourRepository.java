package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Workinghour;


@Repository
public interface WorkinghourRepository extends JpaRepository<Workinghour, String> {

    @Modifying
    @Query("DELETE FROM Workinghour w WHERE w.workinghour_id IN :workCodes")
	void deleteByworkCodes(@Param("workCodes") List<String> workCodes);


}
