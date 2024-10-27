package com.baraanajjar.webSocket.room;

import com.baraanajjar.webSocket.repository.AppRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class ChatRoomService {

    private final AppRoomRepository appRoomRepository;

    @Autowired
    public ChatRoomService(AppRoomRepository AppRoomRepository) {
        this.appRoomRepository = AppRoomRepository;
    }

    public ResponseEntity<Map<String, Object>> createRoom() {
        String roomId = generateRoomId();
        Room Room = new Room(roomId, String.format("%06d", new Random().nextInt(1000000)));
        Room savedRoom = appRoomRepository.save(Room);

        // Create the response body
        Map<String, Object> response = new HashMap<>();
        response.put("roomId", savedRoom.getRoomId());
        response.put("password", savedRoom.getPassword());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public Room getRoomById(String roomId) {
        return appRoomRepository.findByRoomId(roomId);
    }

    public void removeRoom(String roomId) {
        appRoomRepository.deleteByRoomId(roomId);
    }

    public List<Room> getAllRooms() {
        return appRoomRepository.findAll();
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private String generateRoomId() {
        StringBuilder roomId = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            roomId.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return roomId.toString();
    }

}
