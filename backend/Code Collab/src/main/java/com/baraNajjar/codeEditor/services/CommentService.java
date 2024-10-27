package com.baraNajjar.codeEditor.services;


import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CommentService {

    @Bean
    public RestTemplate commentRestTemplate() {
        return new RestTemplate();
    }

    private RestTemplate restTemplate = new RestTemplate();


    private final String commentsUrl = "http://localhost:8081/comment";
    public Map<String, Object> fetchComments(Long project_id) {
        return restTemplate.getForObject(commentsUrl+"/read?project_id="+project_id, Map.class);
    }
    public ResponseEntity<Map<String, Object>> postComment(long userID, Map<String, String> requestBody) {
        Map<String, Object> responseBody = restTemplate.postForObject(commentsUrl + "/post?userID=" + userID, requestBody, Map.class);
        return ResponseEntity.ok(responseBody);
    }


}
