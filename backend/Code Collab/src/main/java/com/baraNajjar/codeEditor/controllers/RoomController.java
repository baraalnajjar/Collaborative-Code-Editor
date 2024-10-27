package com.baraNajjar.codeEditor.controllers;


import com.baraNajjar.codeEditor.services.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('EDITOR','VIEWER')")
    public ResponseEntity<Map<String, Object>> createRoom(@RequestParam String projectName) {
        Map<String, Object> response = roomService.createRoom(projectName);
        return ResponseEntity.ok(response);
    }

}
