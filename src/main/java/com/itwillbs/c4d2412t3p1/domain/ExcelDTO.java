package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;
import java.util.Map;


import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class ExcelDTO {
	List<Map<String, Object>> headers;
    List<Map<String, Object>> rows;
    String title;
}
