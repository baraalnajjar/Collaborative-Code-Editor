package com.baraanajjar.webSocket.controllers;

import com.baraanajjar.webSocket.room.Room;
import com.baraanajjar.webSocket.room.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final ChatRoomService chatRoomService;

    public RoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestParam String projectName) {
        return chatRoomService.createRoom();
    }
    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable String roomId) {
        return chatRoomService.getRoomById(roomId);
    }

    @GetMapping("/all")
    public List<Room> getAllRooms() {
        return chatRoomService.getAllRooms();
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable String roomId) {
        chatRoomService.removeRoom(roomId);
    }
}
