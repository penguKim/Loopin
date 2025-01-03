package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
	Integer getCurrentSequenceForYear(int year);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO LOG_CODE_SEQUENCE (year, sequence_number) VALUES (:year, :sequence) "
			+ "ON DUPLICATE KEY UPDATE sequence_number = :sequence", nativeQuery = true)
	void updateSequenceForYear(int year, int sequence);

	// 모든 로그와 직원 ID를 조회
	@Query("SELECT LO FROM Log LO JOIN FETCH LO.employee")
	List<Log> findAllLogsWithEmployee();

}
