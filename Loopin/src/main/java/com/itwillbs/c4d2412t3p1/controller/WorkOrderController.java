package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.itwillbs.c4d2412t3p1.entity.Workorder;
import com.itwillbs.c4d2412t3p1.repository.WorkorderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Controller
@AllArgsConstructor
@Log
public class WorkOrderController {
	
	private final WorkorderRepository workorderRepository;

    // 페이지 이동
    @GetMapping("/workorder_list")
    public String workOrderListPage() {
        return "productplan/workorder_list"; 
        // templates/productplan/workorder_list.html
    }

    // 작업지시 목록 조회
    @GetMapping("/select_WORKORDER_list")
    public ResponseEntity<List<Workorder>> selectWorkOrderList() {
        List<Workorder> list = workorderRepository.findAll();
        return ResponseEntity.ok(list);
    }

    // 작업지시 저장
    @PostMapping("/save_WORKORDER")
    public ResponseEntity<String> saveWorkOrder(@RequestBody Workorder req) {
        // req.getWorkorder_cd() ...
        workorderRepository.save(req);
        return ResponseEntity.ok("SUCCESS");
    }
	
}
