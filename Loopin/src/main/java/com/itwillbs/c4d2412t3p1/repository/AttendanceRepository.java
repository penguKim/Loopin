package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.AttendancePK;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendancePK> {

	
}
