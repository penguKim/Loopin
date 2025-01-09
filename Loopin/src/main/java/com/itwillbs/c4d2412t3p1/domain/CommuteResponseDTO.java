package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommuteResponseDTO {
	
	List<WorkinghourDTO> workinghourList;
	String workinghour_id;

	
}
