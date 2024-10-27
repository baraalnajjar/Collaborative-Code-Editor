package com.baraNajjar.codeEditor.services;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service

public class CodeService {


    @Bean
    public RestTemplate codeRestTemplate() {
        return new RestTemplate();
    }

    private RestTemplate restTemplate = new RestTemplate();
    private final String codesUrl = "http://localhost:8081/code";
    public ResponseEntity<Map<String, String>> editCode( String projectName ,  Map<String, String> requestBody) {
        Map<String, String> responseBody =restTemplate.postForObject(codesUrl+"/edit"+"?projectName="+projectName,requestBody, Map.class);
        return ResponseEntity.ok(responseBody);
    }
    public ResponseEntity<String> executeCode( String language ,  Map<String, String> requestBody) {
        String responseBody =restTemplate.postForObject(codesUrl+"/execute"+"?language="+language,requestBody, String.class);
        return ResponseEntity.ok(responseBody);
    }

    public ResponseEntity<Map<String, String>> revertCode(String projectName, Map<String, String> requestBody) {
        Map<String, String> responseBody =restTemplate.postForObject(codesUrl+"/revert"+"?projectName="+projectName,requestBody, Map.class);
        return ResponseEntity.ok(responseBody);
    }

    public ResponseEntity<List<Map<String, String>>> getLogsByProjectName(String projectName) {
        List<Map<String, String>> logs = restTemplate.getForObject(
                codesUrl + "/logs?projectName=" + projectName,
                List.class
        );
        return ResponseEntity.ok(logs);
    }
}
