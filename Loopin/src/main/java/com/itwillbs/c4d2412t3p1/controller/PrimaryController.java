package com.itwillbs.c4d2412t3p1.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.PrimaryRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class PrimaryController {

	private final PrimaryService primaryService;
	private final CommonService commonService;
	

	// 창고 관리 --------------------------------------------
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/warehouse_list")
	public String warehouse_list(Model model) {
	    
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list("USE", "USEYN", "WHTYPE");
		
		model.addAttribute("commonList", commonList);
		
		return "/primary/warehouse_list";
	}
	
	// 창고 조회
	@ResponseBody
	@PostMapping("/select_WAREHOUSE_list")
	public Map<String, Object> select_WAREHOUSE_list(@RequestBody PrimaryRequestDTO primaryDTO) {
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		System.out.println(filter);
		Map<String, Object> response = new HashMap<>(); 
		List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	
	
	// 창고 등록
	@LogActivity(value = "등록", action = "창고등록")
	@ResponseBody
	@PostMapping("/insert_WAREHOUSE")
	public ResponseEntity<Map<String, Object>> insert_WAREHOUSE(@RequestBody PrimaryRequestDTO primaryDTO) {
		WarehouseDTO warehouse = primaryDTO.getWarehouse();
		List<WareareaDTO> wareareaList = primaryDTO.getWareareaList();
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	primaryService.insert_WAREHOUSE(warehouse, wareareaList);
	    	
			List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "등록에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 창고 상세 조회
	@ResponseBody
	@PostMapping("/select_WAREHOUSE_detail")
	public ResponseEntity<Map<String, Object>> select_WAREHOUSE_detail(@RequestBody PrimaryRequestDTO primaryDTO) {
		String warehouse_cd = primaryDTO.getWarehouse_cd();
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	Warehouse warehouse =  primaryService.select_WAREHOUSE_detail(warehouse_cd);
	    	response.put("warehouse", warehouse);
	    	List<Warearea> wareareaList = primaryService.select_WAREAREA_list(warehouse_cd);
			System.out.println("wareareaList : " + wareareaList);
			response.put("wareareaList", wareareaList);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 창고 삭제
	@LogActivity(value = "삭제", action = "창고삭제")
	@ResponseBody
	@PostMapping("/delete_WAREHOUSE")
	public ResponseEntity<Map<String, Object>> delete_WAREHOUSE(@RequestBody PrimaryRequestDTO primaryDTO) {
		List<WarehouseDTO> warehouseList = primaryDTO.getWarehouseList();
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		Map<String, Object> response = new HashMap<>();
		
		try {
			primaryService.delete_WAREHOUSE(warehouseList);
			
			
			List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "창고 삭제 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	// 창고코드 중복 체크
	@ResponseBody
	@PostMapping("/check_WAREHOUSE_CD")
	public Map<String, Object> check_WAREHOUSE_CD(@RequestBody PrimaryRequestDTO primaryDTO) {
		String warehouse_cd = primaryDTO.getWarehouse_cd();
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean check = primaryService.check_WAREHOUSE_CD(warehouse_cd); // 중복이면 false
			System.out.println("cpzmsms : " + check);
			response.put("result", check);
			if(!check) {
				response.put("title", "코드명 중복");
				response.put("msg", "이미 사용중인 코드입니다.");
			}
		} catch (Exception e) {
			response.put("msg", "체크에 실패했습니다.");
		}
		return response;
	}
	
	
	// 품목 관리 --------------------------------------------
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/product_list")
	public String product_list(Model model) {
	    
		List<Map<String, String>> warehouseList = primaryService.select_WAREHOUSE_code();
		
		model.addAttribute("warehouseList", warehouseList);
		
		return "/primary/product_list";
	}
	
	// 품목 그룹 조회
	@ResponseBody
	@PostMapping("/select_PRODUCT_group")
	public Map<String, Object> select_PRODUCT_group(@RequestBody PrimaryRequestDTO primaryDTO) {
		Map<String, Object> response = new HashMap<>(); 
		Map<String, Object> data = new HashMap<>();
    	Map<String, List<Common_codeDTO>> product_cc = commonService.select_COMMON_list(primaryDTO.getProduct_gc());
		System.out.println("조회하냐!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if (!product_cc.isEmpty()) {
			response.put("result", true);
			data.put("contents", product_cc.get(product_cc.keySet().iterator().next()));
		} else {
			response.put("result", false);
		}
		response.put("data", data);
		
		return response;
	}
	
	
	// 품목 리스트 조회
	@ResponseBody
	@PostMapping("/select_PRODUCT_list")
	public Map<String, Object> select_PRODUCT_list(@RequestBody PrimaryRequestDTO primaryDTO) {
		ProductFilterRequest filter = primaryDTO.getProductFilter();
		Map<String, Object> response = new HashMap<>(); 
		Map<String, Object> data = new HashMap<>();
		System.out.println(filter);
			List<ProductDTO> list = primaryService.select_PRODUCT_list(filter);			
			data.put("contents", list);
		
		response.put("result", true);
		response.put("data", data);
		
		return response;
	}
	
	// 자재 리스트 조회
	@ResponseBody
	@PostMapping("/select_MATERIAL_list")
	public Map<String, Object> select_MATERIAL_list(@RequestBody PrimaryRequestDTO primaryDTO) {
		ProductFilterRequest filter = primaryDTO.getProductFilter();
		Map<String, Object> response = new HashMap<>(); 
		Map<String, Object> data = new HashMap<>();
		
		List<MaterialDTO> list = primaryService.select_MATERIAL_list(filter);		
		data.put("contents", list);
		response.put("result", true);
		response.put("data", data);
		
		return response;
	}
	
	// 품목 등록
	@LogActivity(value = "등록", action = "품목등록")
	@ResponseBody
	@PostMapping("/insert_PRODUCT")
	public ResponseEntity<Map<String, Object>> insert_PRODUCT(@RequestPart(value = "requestData") PrimaryRequestDTO primaryDTO,
		    @RequestPart(value = "image", required = false) MultipartFile image) {
		ProductDTO product = primaryDTO.getProduct();
		List<String> sizeList = primaryDTO.getSizeList();
		List<String> colorList = primaryDTO.getColorList();
		ProductFilterRequest filter = new ProductFilterRequest();
		filter.setProduct_gc(product.getProduct_gc());		
		Map<String, Object> response = new HashMap<>();
		
	    try {
        	primaryService.insert_PRODUCT(product, image, sizeList, colorList);	        	
	    	
			List<ProductDTO> list = primaryService.select_PRODUCT_list(filter);
			response.put("list", list);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 자재 등록
	@LogActivity(value = "등록", action = "자재등록")
	@ResponseBody
	@PostMapping("/insert_MATERIAL")
	public ResponseEntity<Map<String, Object>> insert_MATERIAL(@RequestPart(value = "requestData") PrimaryRequestDTO primaryDTO,
		    @RequestPart(value = "image", required = false) MultipartFile image) {
		MaterialDTO material = primaryDTO.getMaterial();
		ProductFilterRequest filter = new ProductFilterRequest();
		filter.setProduct_gc(material.getMaterial_gc());
		Map<String, Object> response = new HashMap<>();
		
	    try {
        	primaryService.insert_MATERIAL(material, image);	    
	    	
			List<MaterialDTO> list = primaryService.select_MATERIAL_list(filter);
			response.put("list", list);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 품목코드 중복 체크
	@ResponseBody
	@PostMapping("/check_PRODUCT_CD")
	public Map<String, Object> check_PRODUCT_CD(@RequestBody PrimaryRequestDTO primaryDTO) {
		String product_cd = primaryDTO.getProduct_cd();
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean check = primaryService.check_PRODUCT_CD(product_cd); // 중복이면 false
			response.put("result", check);
			if(!check) {
				response.put("title", "코드명 중복");
				response.put("msg", "이미 사용중인 코드입니다.");
			}
		} catch (Exception e) {
			response.put("msg", "체크에 실패했습니다.");
		}
		return response;
	}
	
	// 자재코드 중복 체크
	@ResponseBody
	@PostMapping("/check_MATERIAL_CD")
	public Map<String, Object> check_MATERIAL_CD(@RequestBody PrimaryRequestDTO primaryDTO) {
		String material_cd = primaryDTO.getMaterial_cd();
		System.out.println("zhemsms : " + material_cd);
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean check = primaryService.check_MATERIAL_CD(material_cd); // 중복이면 false
			response.put("result", check);
			if(!check) {
				response.put("title", "코드명 중복");
				response.put("msg", "이미 사용중인 코드입니다.");
			}
		} catch (Exception e) {
			response.put("msg", "체크에 실패했습니다.");
		}
		return response;
	}
	
	// 품목소분류 조회
	@ResponseBody
	@PostMapping("/select_PRODUCT_CC")
	public ResponseEntity<Map<String, Object>> select_PRODUCT_CC(@RequestBody PrimaryRequestDTO primaryDTO) {
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	Map<String, List<Common_codeDTO>> product_cc = commonService.select_COMMON_list(primaryDTO.getProduct_gc());
	    	if (!product_cc.isEmpty()) {
	    	    response.put("list", product_cc.get(product_cc.keySet().iterator().next()));
	    	}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "등록된 코드가 없습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 제품 상세 조회
	@ResponseBody
	@PostMapping("/select_PRODUCT_detail")
	public ResponseEntity<Map<String, Object>> select_PRODUCT_detail(@RequestBody PrimaryRequestDTO primaryDTO) {
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	ProductFilterRequest filter = new ProductFilterRequest();
	    	filter.setProduct_cd(primaryDTO.getProduct_cd());
	    	filter.setProduct_cc(primaryDTO.getProduct_cc());
	    	List<ProductDTO> list = primaryService.select_PRODUCT_list(filter);
	        if (!list.isEmpty()) {
	            response.put("product", list.get(0));
	            Map<String, List<Common_codeDTO>> product_cc = commonService.select_COMMON_list(list.get(0).getProduct_gc());
		    	if (!product_cc.isEmpty()) {
		    	    String key = product_cc.keySet().iterator().next();
		    	    List<Common_codeDTO> firstList = product_cc.get(key);
		    	    response.put("product_cc", firstList);
		    	}
	        }
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 자재 상세 조회
	@ResponseBody
	@PostMapping("/select_MATERIAL_detail")
	public ResponseEntity<Map<String, Object>> select_MATERIAL_detail(@RequestBody PrimaryRequestDTO primaryDTO) {
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	ProductFilterRequest filter = new ProductFilterRequest();
	    	filter.setProduct_cd(primaryDTO.getMaterial_cd());
	    	filter.setProduct_cc(primaryDTO.getMaterial_cc());
	    	List<MaterialDTO> list = primaryService.select_MATERIAL_list(filter);
	        if (!list.isEmpty()) {
	        	System.out.println(list.get(0).toString());
	            response.put("material", list.get(0));
	            Map<String, List<Common_codeDTO>> product_cc = commonService.select_COMMON_list(list.get(0).getMaterial_gc());
		    	if (!product_cc.isEmpty()) {
		    	    String key = product_cc.keySet().iterator().next();
		    	    List<Common_codeDTO> firstList = product_cc.get(key);
		    	    response.put("product_cc", firstList);
		    	}
	        }
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 제품 삭제
	@LogActivity(value = "삭제", action = "제품삭제")
	@ResponseBody
	@PostMapping("/delete_PRODUCT")
	public ResponseEntity<Map<String, Object>> delete_PRODUCT(@RequestBody PrimaryRequestDTO primaryDTO) {
		List<ProductDTO> productList = primaryDTO.getProductList();
		ProductFilterRequest filter = new ProductFilterRequest();
		filter.setProduct_gc(primaryDTO.getProduct_gc());	
		Map<String, Object> response = new HashMap<>();
		
		try {
			primaryService.delete_PRODUCT(productList);
			
			
			List<ProductDTO> list = primaryService.select_PRODUCT_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "창고 삭제 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	// 자재 삭제
	@LogActivity(value = "삭제", action = "자재삭제")
	@ResponseBody
	@PostMapping("/delete_MATERIAL")
	public ResponseEntity<Map<String, Object>> delete_MATERIAL(@RequestBody PrimaryRequestDTO primaryDTO) {
		List<MaterialDTO> materialList = primaryDTO.getMaterialList();
		ProductFilterRequest filter = new ProductFilterRequest();
		filter.setProduct_gc(primaryDTO.getMaterial_gc());
		Map<String, Object> response = new HashMap<>();
		
		try {
			primaryService.delete_MATERIAL(materialList);
			
			List<MaterialDTO> list = primaryService.select_MATERIAL_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "창고 삭제 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
}