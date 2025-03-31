package com.itwillbs.c4d2412t3p1.logging;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogParser {

    private final ObjectMapper objectMapper;

    // 미리 ObjectReader를 생성하여 재사용
    private ObjectReader logDataReader;
    private ObjectReader nestedJsonReader;

    @PostConstruct
    public void init() {
        // 한 번만 생성하여 재사용 (타입정보는 한 번만 설정)
        logDataReader = objectMapper.readerFor(new TypeReference<Map<String, Object>>() {});
        nestedJsonReader = objectMapper.readerFor(new TypeReference<Map<String, Object>>() {});
    }

    public String parseLogDetails(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return "log_jd 값이 비어 있습니다.";
        }
        try {
            // 최상위 JSON 파싱 (재사용하는 ObjectReader 사용)
            Map<String, Object> logData = logDataReader.readValue(jsonString);

            // "반환값" 추출 및 파싱
            String nestedJsonString = (String) logData.get("반환값");
            if (nestedJsonString == null) {
                return "반환값이 없습니다.";
            }

            // 반환값 내부 JSON 파싱 (재사용하는 ObjectReader 사용)
            Map<String, Object> nestedJson = nestedJsonReader.readValue(nestedJsonString);

            // body 필드 처리
            Object body = nestedJson.get("body");
            if (body == null) {
                return "body 데이터가 없습니다.";
            }

            // body가 List 인 경우
            if (body instanceof List<?>) {
                List<Map<String, Object>> bodyList = (List<Map<String, Object>>) body;
                return bodyList.stream()
                        .map(entry -> entry.entrySet().stream()
                                .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                                .collect(Collectors.joining(", ", "[", "]")))
                        .collect(Collectors.joining("\n\n"));
            }
            // body가 Map 인 경우
            else if (body instanceof Map<?, ?>) {
                Map<String, Object> bodyMap = (Map<String, Object>) body;
                return bodyMap.entrySet().stream()
                        .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(", ", "[", "]"));
            } else {
                return "body 데이터가 예상한 구조와 다릅니다.";
            }
        } catch (Exception e) {
            // 오류 로그 출력 및 간략한 오류 메시지 반환
            System.err.println("JSON 파싱 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "파싱 오류 발생: " + e.getMessage();
        }
    }
}
