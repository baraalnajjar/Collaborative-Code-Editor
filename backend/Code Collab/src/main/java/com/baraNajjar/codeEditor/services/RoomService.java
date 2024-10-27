package com.baraNajjar.codeEditor.services;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class RoomService {

    @Bean
    public RestTemplate roomRestTemplate() {
        return new RestTemplate();
    }

    private RestTemplate restTemplate = new RestTemplate();
    private final String roomsUrl = "http://localhost:8082/room";
    public Map<String, Object> createRoom(String projectName) {
        return restTemplate.postForObject(roomsUrl+"/create?projectName="+projectName,"" ,Map.class);
    }

}
