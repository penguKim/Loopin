package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Member;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, String> {

	@Query("SELECT h FROM Holiday h WHERE h.holiday_dt BETWEEN :startDate AND :endDate ORDER BY h.holiday_dt")
    List<Holiday> findHolidaysInMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
	
}