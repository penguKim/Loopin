package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Equipment;
import com.itwillbs.c4d2412t3p1.mapper.EquipmentMapper;
import com.itwillbs.c4d2412t3p1.repository.EquipmentRepository;
import com.itwillbs.c4d2412t3p1.repository.ProcessRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class EquipmentService {
	
	private final UtilService util;
	private final EquipmentRepository equipmentRepository;
	private final EquipmentMapper equipmentMapper;
	
	private final EntityManager entityManager;
	
	// 제품 조회
	public List<Common_codeDTO> select_PRODUCT_list() {
		return equipmentRepository.select_PRODUCT_list();
	}
	
	// 설비 조회
	public List<EquipmentDTO> select_EQUIPMENT(EquipmentFilterRequest filter) {
		return equipmentMapper.select_EQUIPMENT(filter);
	}
	
	// 설비 등록, 수정
	public void insert_EQUIPMENT(EquipmentDTO equipment, MultipartFile image) throws IOException {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
	    if (equipment.getEquipment_cd() == null || equipment.getEquipment_cd().isEmpty()) {
	    	String sequenceNumber = equipmentRepository.nextEquipment_cd(); // '0001'
            equipment.setEquipment_cd("EQ-" + sequenceNumber); // 'EQ-0001'
	    }
	    
	    Optional<Equipment> equipmentImage = equipmentRepository.findById(equipment.getEquipment_cd());
	    if (equipmentImage.isPresent() && (image == null || image.isEmpty())) {
	        equipmentImage.ifPresent(ex -> {
				try {
					util.deleteFile(ex.getEquipment_pc());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
	    }

	    // 새 파일 업로드 처리
	    if (image != null && !image.isEmpty()) {
	        util.setFile("EQUIPMENT", image, equipment::setEquipment_pc);
	    } else if (image == null) {
	    	equipment.setEquipment_pc(null);
	    }
	    
        if(equipment.getEquipment_cd() != null) {
        	equipment.setEquipment_uu(regUser);
        	equipment.setEquipment_ud(time);
        } 
        if(equipment.getEquipment_cd() == null) {
        	equipment.setEquipment_ru(regUser);
        	equipment.setEquipment_rd(time);
        }
        
        equipmentRepository.save(Equipment.setEquipment(equipment));
    }

	public void delete_EQUIPMENT(List<EquipmentDTO> equipmentList) {
		
		List<String> equipmentCds = equipmentList.stream()
			.map(EquipmentDTO::getEquipment_cd)
			.collect(Collectors.toList());

		equipmentRepository.deleteAllById(equipmentCds);
	}

	
	
	
}
