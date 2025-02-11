package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Process;

@Repository
public interface ProcessRepository extends JpaRepository<Process, String> {

	List<Map<String, Object>> save(Map<String, Object> regidata);

}
