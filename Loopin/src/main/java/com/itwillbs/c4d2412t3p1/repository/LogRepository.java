package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Log;

import jakarta.transaction.Transactional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

//	// 특정 log_cd로 로그 조회
//	@Query("SELECT LO FROM Log LO JOIN FETCH LO.employee WHERE LO.log_cd = :log_cd")
//	Log findByLogCdWithEmployee(@Param("log_cd") String log_cd);

	// 연도별 시퀀스 관리
	@Query(value = "SELECT MAX(sequence_number) FROM LOG_CODE_SEQUENCE WHERE year = :year", nativeQuery = true)
	Integer getCurrentSequenceForYear(@Param("year") int year);

	@Modifying
	@Transactional
	@Query(value = "MERGE INTO LOG_CODE_SEQUENCE target " +
	               "USING (SELECT :year AS year, :sequence AS sequence_number FROM dual) source " +
	               "ON (target.year = source.year) " +
	               "WHEN MATCHED THEN " +
	               "UPDATE SET target.sequence_number = source.sequence_number " +
	               "WHEN NOT MATCHED THEN " +
	               "INSERT (year, sequence_number) " +
	               "VALUES (source.year, source.sequence_number)", nativeQuery = true)
	void updateSequenceForYear(@Param("year") int year, @Param("sequence") int sequence);
	
	 @Query("SELECT l FROM Log l LEFT JOIN FETCH l.employee e")
	 List<Log> findAllLogsWithEmployee();
	
//	// 모든 로그와 직원 ID를 조회
//	@Query("SELECT LO FROM Log LO JOIN FETCH LO.employee")
//	List<Log> findAllLogsWithEmployee();

}
