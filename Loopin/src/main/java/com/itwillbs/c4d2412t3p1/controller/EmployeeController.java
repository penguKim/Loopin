package com.itwillbs.c4d2412t3p1.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@RequiredArgsConstructor
@Controller
@Log
public class EmployeeController {

	private final EmployeeService employeeService;
	
    @Value("${file.upload-dir}") // application.properties에서 설정한 업로드 디렉터리 경로
    private String uploadDir;

    /**
     * 파일 업로드 처리
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 업로드된 파일 이름
            String fileName = file.getOriginalFilename();

            // 파일 저장 경로
            Path uploadPath = Paths.get(uploadDir).resolve(fileName);

            // 파일 저장
            file.transferTo(uploadPath.toFile());

            System.out.println("File saved at: " + uploadPath.toString());
            
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed!");
        }
    }

    /**
     * 업로드된 파일 반환
     */
    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Type", Files.probeContentType(filePath))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
    
    
    // 인사카드 페이지로 이동
	@GetMapping("/employee_list")
	public String employee_list(Model model) {

		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String role = employeeDetails.getEmployee_rl();
		
		// 부서코드 가져오기 
		model.addAttribute("dept_list", employeeService.selectCommonList("DEPARTMENT"));

		// 직위코드 가져오기 
		model.addAttribute("grade_list", employeeService.selectCommonList("POSITION"));
		
		// 부서장 유무 가져오기
		model.addAttribute("DPType_list", employeeService.selectCommonList("DPTYPE"));

		// 인사카드 사용여부 가져오기
		model.addAttribute("useyn_list", employeeService.selectCommonList("USEYN"));

		// 롤값 가져오기 
		model.addAttribute("role", role);

		return "/employee/employee_list";
	}

	
	// 인사 카드 조회
	@GetMapping("/select_EMPLOYEE")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_EMPLOYEE() {
		
		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
	    String currentCd = employeeDetails.getEmployee_cd(); // 현재 사용자의 코드
	    String currentRole = employeeDetails.getEmployee_rl(); // 현재 사용자의 권한

	    
	    List<Employee> employees;

	    // 관리자일 경우 모든 직원 정보 조회
	    if (currentRole.contains("admin") || currentRole.contains("developer")) {
	        employees = employeeService.findAll(); // 모든 직원 정보 조회
	    } else {
	        // 일반 사용자일 경우 본인 정보만 조회
	        employees = employeeService.findByEmployeeCd(currentCd); // 본인 정보 조회
	    }

	    // 공통 응답 생성
	    List<Map<String, Object>> response = employees.stream().map(employee -> {
	        Map<String, Object> row = new HashMap<>();
	        Boolean employee_mg = employee.getEmployee_mg();
	        
	        row.put("employee_cd", employee.getEmployee_cd());
	        row.put("employee_id", employee.getEmployee_id());
	        row.put("employee_pw", employee.getEmployee_pw());
	        row.put("employee_dp", employee.getEmployee_dp());
	        row.put("employee_gd", employee.getEmployee_gd());
	        row.put("employee_hd", employee.getEmployee_hd());
	        row.put("employee_rd", employee.getEmployee_rd());
	        row.put("employee_rr", employee.getEmployee_rr());
	        row.put("employee_cg", employee.getEmployee_cg());
	        row.put("employee_nt", employee.getEmployee_nt());
	        row.put("employee_nm", employee.getEmployee_nm());
	        row.put("employee_bd", employee.getEmployee_bd());
	        row.put("employee_ad", employee.getEmployee_ad());
	        row.put("employee_sb", employee.getEmployee_sb());
	        row.put("employee_ph", employee.getEmployee_ph());
	        row.put("employee_em", employee.getEmployee_em());
	        row.put("employee_pi", employee.getEmployee_pi());
	        row.put("employee_bs", employee.getEmployee_bs());
	        row.put("employee_bk", employee.getEmployee_bk());
	        row.put("employee_an", employee.getEmployee_an());
	        row.put("employee_dt", employee.getEmployee_dt());
	        row.put("employee_wr", employee.getEmployee_wr());
	        row.put("employee_wd", employee.getEmployee_wd());
	        row.put("employee_mf", employee.getEmployee_mf());
	        row.put("employee_md", employee.getEmployee_md());
	        row.put("employee_mg", employee_mg != null && employee_mg);
	        row.put("employee_rl", employee.getEmployee_rl());
	        row.put("employee_us", employee.getEmployee_us());
	        
	        return row;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/insert_EMPLOYEE")
	public ResponseEntity<Map<String, String>> insert_EMPLOYEE(
		    @RequestPart("employeeDTO") EmployeeDTO employeeDTO, // DTO 받기
		    @RequestPart(value = "employee_pi", required = false) MultipartFile employee_pi) {
		Map<String, String> response = new HashMap<>();
		
		String employee_id = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {
	       
			// 파일 업로드 처리
	        if (employee_pi != null && !employee_pi.isEmpty()) {
	        	
	        	// 고유 파일명 생성
	            String uniqueFileName = UUID.randomUUID().toString() + "_" + employee_pi.getOriginalFilename();
	            Path uploadPath = Paths.get(uploadDir).resolve(uniqueFileName); // 업로드 경로
	           
	            // 업로드 디렉토리 생성
	            if (!Files.exists(uploadPath.getParent())) {
	                Files.createDirectories(uploadPath.getParent());
	            }

	            // 파일 저장
	            Files.copy(employee_pi.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

	            // DTO에 파일명 설정
	            employeeDTO.setEmployee_pi(uniqueFileName);
	        }
			
	        employeeDTO.setEmployee_wr(employee_id);
	        
	        employeeDTO.setEmployee_wd(new Timestamp(System.currentTimeMillis()));
	        // 데이터 저장 처리
			employeeService.insert_EMPLOYEE(employeeDTO, employee_pi);
			
			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
		} catch (Exception e) {
			response.put("message", "데이터 저장 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@PostMapping("/update_EMPLOYEE")
	public ResponseEntity<Map<String, String>> update_EMPLOYEE(
	        @RequestPart("employeeDTO") EmployeeDTO employeeDTO,// DTO 받기
	        @RequestPart(value = "employee_pi", required = false) MultipartFile employee_pi) {

		String employee_id = SecurityContextHolder.getContext().getAuthentication().getName();
		
		
	    Map<String, String> response = new HashMap<>();
	    try {
	    	
	    	String employee_cd = employeeDTO.getEmployee_cd();
	    	
	        if (employee_cd == null) {
	            response.put("message", "데이터 수정 실패: ID(employee_cd)가 전달되지 않았습니다.");
	            return ResponseEntity.badRequest().body(response);
	        }

	        
	        // 기존 데이터 조회
	        Employee employee = employeeService.findEmployeeById(employeeDTO.getEmployee_cd());
	        if (employee == null) {
	            response.put("message", "데이터 수정 실패: 해당 ID의 데이터를 찾을 수 없습니다.");
	            return ResponseEntity.badRequest().body(response);
	        }

	        // 기존 employee_wr 값 유지
	        employeeDTO.setEmployee_wr(employee.getEmployee_wr());

	        // 기존 employee_wd 값을 유지
	        employeeDTO.setEmployee_wd(employee.getEmployee_wd());
	        
	        
	        // 기존 사진 삭제 처리
	        if ("true".equals(employeeDTO.getPhotoDeleted())) { // photoDeleted가 true인 경우
	            employeeService.deleteEmployeePhoto(employeeDTO.getEmployee_cd());
	            employeeDTO.setEmployee_pi(null); // 삭제 후 DTO의 사진 경로 초기화
	        }

	        // 파일 업로드 처리
	        if (employee_pi != null && !employee_pi.isEmpty()) {
	            // 고유 파일명 생성
	            String uniqueFileName = UUID.randomUUID().toString() + "_" + employee_pi.getOriginalFilename();
	            Path uploadPath = Paths.get(uploadDir).resolve(uniqueFileName); // 업로드 경로

	            // 업로드 디렉터리 생성
	            if (!Files.exists(uploadPath.getParent())) {
	                Files.createDirectories(uploadPath.getParent());
	            }

	            // 파일 저장
	            Files.copy(employee_pi.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

	            
	            // DTO에 파일명 설정
	            employeeDTO.setEmployee_pi(uniqueFileName);
	        }

	        employeeDTO.setEmployee_mf(employee_id);
	        
	        employeeDTO.setEmployee_md(new Timestamp(System.currentTimeMillis()));

	        // Service 호출
	        employeeService.update_EMPLOYEE(employeeDTO, employee_pi);

	        // 성공 응답
	        response.put("message", "데이터가 성공적으로 수정되었습니다.");
	        return ResponseEntity.ok(response);

	    } catch (IllegalArgumentException e) {
	        // ID로 조회되지 않는 경우 처리
	        response.put("message", "데이터 수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

	    } catch (Exception e) {
	        // 기타 예외 처리
	        response.put("message", "데이터 수정 실패: 알 수 없는 오류가 발생했습니다. " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	
	
	//	인사발령 삭제
	@PostMapping("/delete_EMPLOYEE")
	public ResponseEntity<Map<String, Object>> delete_EMPLOYEE(@RequestBody Map<String, List<String>> request) {
		
		List<String> employeeCds  = request.get("employee_cds");
		
		log.info("삭제 요청 데이터: " + request.toString());
		
		Map<String, Object> response = new HashMap<>();

		try {
			employeeService.updateEmployeeStatus(employeeCds, false);
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	 
	// 인사현황 차트
	@GetMapping("/employee_chart")
	public String employee_chart(Model model) {
		
		return "/employee/employee_chart";
	}
	

    // 성별 데이터 조회
    @GetMapping("/select_GENDER")
    public ResponseEntity<Map<String, Object>> select_GENDER(
            @RequestParam("start_dt") String startDt,
            @RequestParam("end_dt") String endDt) {
    	
    	
        // 서비스 호출: 시작일과 종료일을 기준으로 데이터 조회
        List<Map<String, Object>> genderStats = employeeService.getEmployeeGenderStatsByDate(startDt, endDt);
    	
    	
        // Toast UI Chart 형식으로 변환
        List<Map<String, Object>> series = genderStats.stream()
            .map(stat -> Map.of(
                "name", stat.get("name"), // "남성" 또는 "여성"
                "data", stat.get("data")  // 인원수
            ))
            .toList();

        Map<String, Object> response = Map.of("series", series);
        
        return ResponseEntity.ok(response);
    }

    // 입/퇴사자 조회 현황 데이터 조회
    @GetMapping("/select_HDRD")
    public ResponseEntity<Map<String, Object>> select_HDRD(
            @RequestParam("start_dt") String startDt,
            @RequestParam("end_dt") String endDt) {

        // 서비스 호출: 입사자와 퇴사자 데이터를 조회
        Map<String, List<?>> hireAndRetireStats = employeeService.getHireAndRetireStatsByDate(startDt, endDt);

        
        System.out.println("@@@@@@@@@@@" + hireAndRetireStats);
        
        // 데이터 매핑
        Map<String, Object> response = Map.of(
                "categories", hireAndRetireStats.get("categories"),  // List<String>
                "series", List.of(
                        Map.of("name", "입사자", "data", hireAndRetireStats.get("hireData")),  // List<Integer>
                        Map.of("name", "퇴사자", "data", hireAndRetireStats.get("retireData")) // List<Integer>
                )
        );

        System.out.println("@@@@@@@@@@@" + response);
        
        return ResponseEntity.ok(response);
    }


    
    // 부서별 인원 현황 데이터 조회
    @GetMapping("/select_DEPT")
    public ResponseEntity<Map<String, Object>> select_DEPT(
    		@RequestParam("start_dt") String startDt,
    		@RequestParam("end_dt") String endDt) {
    	
    	
    	// 서비스 호출: 시작일과 종료일을 기준으로 데이터 조회
    	List<Map<String, Object>> deptStats = employeeService.getEmployeeDeptStatsByDate(startDt, endDt);
    	
    	
    	// Toast UI Chart 형식으로 변환
    	List<Map<String, Object>> series = deptStats.stream()
    			.map(stat -> Map.of(
    					"name", stat.get("name"), // 각 부서
    					"data", stat.get("data")  // 인원수
    					))
    			.toList();
    	
    	Map<String, Object> response = Map.of("series", series);
    	
    	return ResponseEntity.ok(response);
    }
    
    
    // 직위별 조회 현황 데이터 조회
    @GetMapping("/select_POSI")
    public ResponseEntity<Map<String, Object>> select_POSI(
            @RequestParam("start_dt") String startDt,
            @RequestParam("end_dt") String endDt) {

        // 서비스 호출: 시작일과 종료일을 기준으로 데이터 조회
        List<Map<String, Object>> posiStats = employeeService.getEmployeePosiStatsByDate(startDt, endDt);

        // 직위명과 인원 수를 각각 카테고리와 데이터로 분리
        List<String> categories = posiStats.stream()
                .map(stat -> (String) stat.get("name"))  // 직위명 추출
                .collect(Collectors.toList());

        List<Integer> data = posiStats.stream()
                .map(stat -> ((BigDecimal) stat.get("data")).intValue())  // 인원 수(BigDecimal을 Integer로 변환)
                .collect(Collectors.toList());

        // Toast UI Chart 형식으로 변환
        Map<String, Object> response = Map.of(
                "categories", categories,
                "data", data
        );

        return ResponseEntity.ok(response);
    }
    
    
    
    // 아이디 유효성 검사
    @PostMapping("/check_employee_id")
    @ResponseBody
    public Map<String, Boolean> checkEmployeeId(@RequestBody Map<String, String> request) {
        Map<String, Boolean> response = new HashMap<>();
        String employee_id = request.get("employee_id");  // JSON으로 전달된 값 받기
        try {
            boolean isValid = employeeService.isEmployeeIdAvailable(employee_id); // 중복 여부 확인
            response.put("isValid", isValid);
        } catch (Exception e) {
            // 예외 로그 출력
            e.printStackTrace();
            response.put("isValid", false); // 예외 발생 시 false 처리
        }
        return response;
    }




    

}
