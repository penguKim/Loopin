package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.AttendancePK;
import com.itwillbs.c4d2412t3p1.entity.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {

	@Query(value = "SELECT 'EQ-' || LPAD(SEQ_EQUIPMENT_CD.NEXTVAL, 4, '0') FROM DUAL", nativeQuery = true)
    String nextEquipment_cd();

}
