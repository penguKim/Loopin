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
            // 1. 최상위 JSON 파싱
            Map<String, Object> logData = objectMapper.readValue(jsonString, new TypeReference<>() {});

            // 2. "반환값" 추출 및 다시 파싱
            String nestedJsonString = (String) logData.get("반환값");
            if (nestedJsonString == null) {
                return "반환값이 없습니다.";
            }

            // 3. 반환값 내부 JSON 파싱
            Map<String, Object> nestedJson = objectMapper.readValue(nestedJsonString, new TypeReference<>() {});
            List<Map<String, Object>> body = (List<Map<String, Object>>) nestedJson.get("body");

            if (body == null || body.isEmpty()) {
                return "body 데이터가 없습니다.";
            }

            // 4. body 데이터 가공 - 대괄호로 감싸기
            return body.stream()
                    .map(entry -> entry.entrySet().stream()
                            .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                            .collect(Collectors.joining(", ", "[", "]")))
                    .collect(Collectors.joining("\n"));

        } catch (Exception e) {
            System.err.println("JSON 파싱 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "파싱 오류 발생: " + e.getMessage();
        }
    }
}
