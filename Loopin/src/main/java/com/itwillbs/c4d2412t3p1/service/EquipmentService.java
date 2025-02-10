package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Equipment;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Material;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.mapper.DashboardMapper;
import com.itwillbs.c4d2412t3p1.repository.EquipmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class EquipmentService {
	
	private final UtilService util;
	private final EquipmentRepository equipmentRepository;
	
	public List<Equipment> findAll() {
		return equipmentRepository.findAll();
	}
	
	// 원자재, 부자재 등록
	public void insert_EQUIPMENT(EquipmentDTO equipment, MultipartFile image) throws IOException {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    String cd = equipment.getEquipment_cd();
	    
	    // 기존 파일 처리
	    Optional<Equipment> equipmentImage = equipmentRepository.findById(cd);
	    if (equipmentImage.isPresent() && (image == null || image.isEmpty())) {
	        equipmentImage.ifPresent(ex -> {
				try {
					util.deleteFile(ex.getEquipment_pc());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	    }
	    
	    equipment.setEquipment_cd(equipmentRepository.nextEquipment_cd());

	    // 새 파일 업로드 처리
	    if (image != null && !image.isEmpty()) {
	        util.setFile("EQUIPMENT", image, equipment::setEquipment_pc);
	    } else if (image == null) {
	    	equipment.setEquipment_pc(null);
	    }
	    
        if(equipment.getEquipment_ru() == null) {
        	equipment.setEquipment_ru(regUser);
        	equipment.setEquipment_rd(time);
        } else {
        	equipment.setEquipment_uu(regUser);
        	equipment.setEquipment_ud(time);
        }
        
        equipmentRepository.save(Equipment.setEquipment(equipment));
    }	
	
}
