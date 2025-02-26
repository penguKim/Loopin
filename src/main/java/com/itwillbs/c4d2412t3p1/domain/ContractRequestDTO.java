package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDTO {
    private ContractDTO contract; // 수주 헤드 데이터
    private List<ContractDetailDTO> details; // 수주 바디 데이터
}
