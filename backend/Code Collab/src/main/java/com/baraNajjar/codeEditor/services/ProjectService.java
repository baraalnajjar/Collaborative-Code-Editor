package com.baraNajjar.codeEditor.services;


import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService {


    @Bean
    public RestTemplate projectRestTemplate() {
        return new RestTemplate();
    }

    private RestTemplate restTemplate = new RestTemplate();

    private final String projectsUrl = "http://localhost:8081/project";

    public Map<String, Object> getLanguages() {
        return restTemplate.getForObject(projectsUrl+"/languages", Map.class);
    }

    public Map<String, Object> getProjectsByLanguage(String language) {
        return restTemplate.getForObject(projectsUrl+"?language="+language, Map.class);
    }
    public Map<String, Object> getID(String project_name) {
        return restTemplate.getForObject(projectsUrl+"/getId"+"?project_name="+project_name, Map.class);
    }
    public Map<String, Object> getCode(String project_name) {
        return restTemplate.getForObject(projectsUrl+"/getCode"+"?project_name="+project_name, Map.class);
    }
    public ResponseEntity<List<String>> getAllVersions(String projectName) {
        ResponseEntity<List<String>> response = restTemplate.exchange(
                projectsUrl + "/" + projectName + "/versions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        return response;
    }

    public ResponseEntity<List<String>> createProject(String projectName,  Map<String, String> requestBody) {
        String url = projectsUrl + "/create?projectName=" + projectName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<String>>() {}
        );
        return responseEntity;
    }


}
