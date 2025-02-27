package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Material;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.mapper.PrimaryMapper;
import com.itwillbs.c4d2412t3p1.repository.MaterialRepository;
import com.itwillbs.c4d2412t3p1.repository.ProductRepository;
import com.itwillbs.c4d2412t3p1.repository.WareareaRepository;
import com.itwillbs.c4d2412t3p1.repository.WarehouseRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class PrimaryService {
	
	private final UtilService util;
	private final PrimaryMapper primaryMapper;
	private final WarehouseRepository warehouseRepository;
	private final ProductRepository productRepository;
	private final MaterialRepository materialRepository;
	private final WareareaRepository wareareaRepository;
	
	private final EntityManager entityManager;

	// 창고 관리 ----------------------------------------------------------------
	public List<WarehouseDTO> select_WAREHOUSE_list(WarehouseFilterRequest filter) {
		return primaryMapper.select_WAREHOUSE_list(filter);
	}

	// 창고 등록
	@Transactional
	public void insert_WAREHOUSE(WarehouseDTO warehouseDTO, List<WareareaDTO> wareareaDTOList) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
        Warehouse warehouse = warehouseRepository.findById(warehouseDTO.getWarehouse_cd())
            .map(existing -> {
                existing.setWarehouse_nm(warehouseDTO.getWarehouse_nm());
                existing.setWarehouse_tp(warehouseDTO.getWarehouse_tp());
                existing.setWarehouse_in(warehouseDTO.getWarehouse_in());
                existing.setWarehouse_us(warehouseDTO.isWarehouse_us());
                existing.setWarehouse_rm(warehouseDTO.getWarehouse_rm());
                existing.setWarehouse_uu(regUser);
                existing.setWarehouse_ud(time);
                return existing;
            })
            .orElseGet(() -> {
                warehouseDTO.setWarehouse_ru(regUser);
                warehouseDTO.setWarehouse_rd(time);
                return Warehouse.setWarehouse(warehouseDTO);
            });
        
        // 창고 등록
        warehouseRepository.save(warehouse);
        if(wareareaDTOList != null) {
            // 기존 구역 삭제
            wareareaRepository.deleteByWarehouseCd(warehouseDTO.getWarehouse_cd());

            List<Warearea> wareareaList = wareareaDTOList.stream()
                .map(warearea -> {
                	warearea.setWarehouse_cd(warehouseDTO.getWarehouse_cd());
                    return Warearea.setWarehouse(warearea);
                })
                .collect(Collectors.toList());
            // 구역 등록
            wareareaRepository.saveAll(wareareaList);
        }
	}
	
	// 창고코드 중복 체크
	public boolean check_WAREHOUSE_CD(String warehouse_cd) {
		return warehouseRepository.findById(warehouse_cd).isEmpty();
	}

	// 창고 상세 조회
	public Warehouse select_WAREHOUSE_detail(String warehouse_cd) {
		return warehouseRepository.findById(warehouse_cd).orElse(null);
	}
	

	// 창고구역 조회
	public List<Warearea> select_WAREAREA_list(String warehouse_cd) {
		return wareareaRepository.findByWarehouseCd(warehouse_cd);
	}

	// 창고 삭제
	@Transactional
	public void delete_WAREHOUSE(List<WarehouseDTO> warehouseList) {
		// 창고에 재고가 있을 경우 삭제안되게 로직 수정
		
	    List<String> warehouseCodes = warehouseList.stream()
	        .map(WarehouseDTO::getWarehouse_cd)
	        .collect(Collectors.toList());
	    
	    wareareaRepository.deleteByWarehouseCdIn(warehouseCodes);
	    
	    warehouseRepository.deleteAllById(warehouseCodes);
	}

	


	
	// 품목 관리 ----------------------------------------------------------------4
	
	// 품목 조회
	public List<ProductDTO> select_PRODUCT_list(ProductFilterRequest filter) {
		return primaryMapper.select_PRODUCT_list(filter);
	}

	// 자재 정보 조회
	public List<MaterialDTO> select_MATERIAL_list(ProductFilterRequest filter) {
		return primaryMapper.select_MATERIAL_list(filter);
	}

	// 창고 목록 조회
	public List<Map<String, String>> select_WAREHOUSE_code() {
		return primaryMapper.select_WAREHOUSE_code();
	}

	// 품목 등록
	@Transactional
	public void insert_PRODUCT(ProductDTO productDTO, MultipartFile image, List<String> sizeList, List<String> colorList) throws IOException {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    String oldFilePath = null;
	    
	    try {
		    // 기존 파일 처리
		    ProductDTO productImage = primaryMapper.select_PRODUCT_PC(productDTO.getProduct_cd());
		    if (productImage != null && productImage.getProduct_pc() != null
		        && (image == null || !image.isEmpty())) {
		    	oldFilePath = productImage.getProduct_pc();
		    }
	
		    // 새 파일 업로드 처리
		    if (image != null && !image.isEmpty()) {
		    	System.out.println("새 파일 등록");
		        util.setFile("PRODUCT", image, productDTO::setProduct_pc);
		    } else if (image == null) {
		        productDTO.setProduct_pc(null);
		    }

		    // 사이즈와 컬러 리스트를 문자열로 변환
		    String sizeListStr = String.join(",", sizeList);
		    String colorListStr = String.join(",", colorList);
	        
            // 저장 프로시저 호출
            StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("UPSERT_PRODUCT_BATCH");
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_product_cd", productDTO.getProduct_cd());
            parameters.put("p_product_nm", productDTO.getProduct_nm());
            parameters.put("p_product_gc", productDTO.getProduct_gc());
            parameters.put("p_product_cc", productDTO.getProduct_cc());
            parameters.put("p_product_gd", productDTO.getProduct_gd());
            parameters.put("p_product_un", productDTO.getProduct_un());
            parameters.put("p_product_pr", productDTO.getProduct_pr());
            parameters.put("p_product_wh", productDTO.getProduct_wh());
            parameters.put("p_product_pc", productDTO.getProduct_pc());
            parameters.put("p_product_rm", productDTO.getProduct_rm());
            parameters.put("p_product_us", productDTO.isProduct_us() ? 1 : 0);
            parameters.put("p_reg_user", regUser);
            parameters.put("p_size_list", sizeListStr);
            parameters.put("p_color_list", colorListStr);
            
            parameters.forEach(query::setParameter);
            
            query.execute();
	        
	        // 모든 작업 성공 후 기존 파일 삭제
	        if (oldFilePath != null) {
	            util.deleteFile(oldFilePath);
	        }

	    } catch (Exception e) {
	        // 실패 시 신규 이미지 삭제
	        if (productDTO.getProduct_pc() != null) {
	            util.deleteFile(productDTO.getProduct_pc());
	        }
	        throw new RuntimeException("등록에 실패했습니다.", e);
	        
        } finally {
        	entityManager.clear();
        }
	}
	
	// 원자재, 부자재 등록
	public void insert_MATERIAL(MaterialDTO material, MultipartFile image) throws IOException {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
	    // 기존 파일 처리
	    MaterialDTO materialImage = primaryMapper.select_MATERIAL_PC(material.getMaterial_cd());
	    if (materialImage != null && materialImage.getMaterial_cc() != null
	        && (image == null || !image.isEmpty())) {
	        util.deleteFile(materialImage.getMaterial_pc());
	    }

	    // 새 파일 업로드 처리
	    if (image != null && !image.isEmpty()) {
	        util.setFile("PRODUCT", image, material::setMaterial_pc);
	    } else if (image == null) {
	        material.setMaterial_pc(null);
	    }
	    
        if(material.getMaterial_ru() == null) {
        	material.setMaterial_ru(regUser);
        	material.setMaterial_rd(time);
        } else {
        	material.setMaterial_uu(regUser);
        	material.setMaterial_ud(time);
        }
        
        materialRepository.save(Material.setMaterial(material));
    }		


	// 품목 중복 체크
	public boolean check_PRODUCT_CD(String product_cd) {
		return productRepository.findByProductCd(product_cd).isEmpty();
	}
	
	// 자재 중복 체크
	public boolean check_MATERIAL_CD(String material_cd) {
		return materialRepository.findByMaterialCd(material_cd).isEmpty();
	}

	// 품목 삭제
	@Transactional
	public void delete_PRODUCT(List<ProductDTO> productList) {
		// 재고가 있거나 공정이 진행중인 품목은 삭제안되게 처리 예정
		
	    List<String> productCodes = productList.stream()
	        .map(ProductDTO::getProduct_cd)
	        .collect(Collectors.toList());
	    
	    productRepository.deleteByProductCdIn(productCodes);
	}

	// 자재 삭제
	@Transactional
	public void delete_MATERIAL(List<MaterialDTO> materialList) {

	    List<String> materialCodes = materialList.stream()
		        .map(MaterialDTO::getMaterial_cd)
		        .collect(Collectors.toList());
	    
	    materialRepository.deleteByMaterialCd(materialCodes);
	}




}
