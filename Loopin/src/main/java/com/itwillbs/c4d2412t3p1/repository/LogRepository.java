package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.entity.Log;
import com.itwillbs.c4d2412t3p1.util.FilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.LogFilterRequest;

import jakarta.transaction.Transactional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

//	// 특정 log_cd로 로그 조회
//	@Query("SELECT LO FROM Log LO JOIN FETCH LO.employee WHERE LO.log_cd = :log_cd")
//	Log findByLogCdWithEmployee(@Param("log_cd") String log_cd);

	// Oracle 시퀀스를 통해 다음 시퀀스 값을 가져옴
	@Query(value = "SELECT LOG_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	@Query("SELECT LO FROM LOG LO LEFT JOIN FETCH LO.employee EM")
	List<Log> findAllLogsWithEmployee();
	
	@Query("SELECT LO FROM LOG LO " +
		       "LEFT JOIN FETCH LO.employee EM " + // EMPLOYEE 테이블과 조인
		       "WHERE " +
		       "(:#{#filterRequest.startDate} IS NULL OR LO.log_od >= :#{#filterRequest.startDate}) AND " +
		       "(:#{#filterRequest.endDate} IS NULL OR LO.log_od <= :#{#filterRequest.endDate}) AND " +
		       "(:#{#filterRequest.employee_id} IS NULL OR EM.employee_id = :#{#filterRequest.employee_id}) AND " +
		       "(:#{#filterRequest.log_sj} IS NULL OR LO.log_sj LIKE CONCAT('%', :#{#filterRequest.log_sj}, '%')) AND " +
		       "(:#{#filterRequest.log_ju} IS NULL OR LO.log_ju = :#{#filterRequest.log_ju}) AND " +
		       "(:#{#filterRequest.log_oi} IS NULL OR LO.log_oi = :#{#filterRequest.log_oi}) AND " +
		       "(:#{#filterRequest.log_bj} IS NULL OR LO.log_bj = :#{#filterRequest.log_bj})")
		List<Log> findLogsByFilter(@Param("filterRequest") FilterRequest.LogFilterRequest filterRequest);

	
//	// 모든 로그와 직원 ID를 조회
//	@Query("SELECT LO FROM Log LO JOIN FETCH LO.employee")
//	List<Log> findAllLogsWithEmployee();

}
