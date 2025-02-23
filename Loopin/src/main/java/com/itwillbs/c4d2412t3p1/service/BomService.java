package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.BomallDTO;
import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.entity.Product;
import com.itwillbs.c4d2412t3p1.mapper.BomMapper;
import com.itwillbs.c4d2412t3p1.repository.BomProcessRepository;
import com.itwillbs.c4d2412t3p1.repository.BomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BomService {
	
	private final BomRepository bR;
	private final BomProcessRepository bpR;
	private final BomMapper bM;
	
	public List<Product> selectPD() {

		List<Product> list = bM.selectPD();
		
		return list;
	}

	public List<BomProcess> selectBomAll() {
		return bM.selectBomAll();
	}

	public Integer checkpcd(String pcd) {
		
		String result = bM.checkpcd(pcd);
		
		if(result == null) {
			return 0;
		}else {
			return 1;
		}
	}

	public List<Process> selectPCs() {
		
		List<Process> list = bM.selectPCs();
		
		return list;
	}

	public List<Product> selectbom(List<String> ckrowpds) {

		List<Product> list = bM.selectbom(ckrowpds);
		
		return list;
	}

	public void insertbom(List<BomallDTO> bom, List<BomallDTO> bompc) {
		
		for (BomallDTO bomit : bom) {
			Bom bomdata = new Bom();
			bomdata.setBom_cd(bomit.getBom_cd());
			bomdata.setBom_am(bomit.getBom_am());
			bomdata.setProduct_cd(bomit.getProduct_cd());
			bomdata.setBomproduct_cd(bomit.getBomproduct_cd());
			bR.save(bomdata);
		}
		for (BomallDTO bomItem : bompc) {
			BomProcess bompcdata = new BomProcess();
			bompcdata.setBomprocess_cd(bomItem.getBomprocess_cd());
			bompcdata.setBomprocess_ap(bomItem.getBomprocess_ap());
			bompcdata.setProcess_cd(bomItem.getProcess_cd());
			bompcdata.setProduct_cd(bomItem.getProduct_cd());
			bompcdata.setBomprocess_rt(bomItem.getBomprocess_rt());
			bompcdata.setBomprocess_er(bomItem.getBomprocess_er());
			bompcdata.setBomprocess_wr(bomItem.getBomprocess_wr());
			bompcdata.setBomprocess_wd(new Timestamp(System.currentTimeMillis()));
			bompcdata.setBomprocess_bg(bomItem.getBomprocess_bg());
			bpR.save(bompcdata);
		}
	}

	public Map<String, Object> selectbom(String bpap, String pdcd, String bpcd, String bppc) {
		
		List<Map<String, Object>> pcgriddata = bM.selectpcsfrompd(bpap, pdcd, bppc);
		List<Map<String, Object>> bomgriddata = bM.selectbomsformpd(pdcd, bpcd);
		
		Map<String, Object> response = new HashMap<>();
		response.put("pcgrid", pcgriddata);
		response.put("bomgrid", bomgriddata);
		
		return response;
	}

	public int deletebom(List<BomallDTO> deletedata) {
		
		int list = bM.deletebom(deletedata);
		
		return list;
	}


}
