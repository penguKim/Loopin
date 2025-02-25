package com.itwillbs.c4d2412t3p1.logging;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogParser {

	private final ObjectMapper objectMapper;

	public String parseLogDetails(String jsonString) {
		try {
			// 최상위 JSON 파싱
			Map<String, Object> logData = objectMapper.readValue(jsonString, new TypeReference<>() {
			});

			// "반환값" 추출 및 파싱
			String nestedJsonString = (String) logData.get("반환값");
			if (nestedJsonString == null) {
				return "반환값이 없습니다.";
			}

			// 반환값 내부 JSON 파싱
			Map<String, Object> nestedJson = objectMapper.readValue(nestedJsonString, new TypeReference<>() {
			});

			// body 필드 처리
			Object body = nestedJson.get("body");
			if (body == null) {
				return "body 데이터가 없습니다.";
			}

			// body가 List인지 Map인지 확인 후 처리
			if (body instanceof List<?>) {
				// body가 List<Map>일 때
				List<Map<String, Object>> bodyList = (List<Map<String, Object>>) body;
				return bodyList.stream()
						.map(entry -> entry.entrySet().stream()
								.map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
								.collect(Collectors.joining(", ", "[", "]")))
						.collect(Collectors.joining("\n\n"));
			} else if (body instanceof Map<?, ?>) {
				// body가 단일 Map일 때
				Map<String, Object> bodyMap = (Map<String, Object>) body;
				return bodyMap.entrySet().stream().map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
						.collect(Collectors.joining(", ", "[", "]"));
			} else {
				// body가 예상치 못한 데이터 타입일 때
				return "body 데이터가 예상한 구조와 다릅니다.";
			}

		} catch (Exception e) {
			System.err.println("JSON 파싱 오류 발생: " + e.getMessage());
			e.printStackTrace();
			return "파싱 오류 발생: " + e.getMessage();
		}
	}

}