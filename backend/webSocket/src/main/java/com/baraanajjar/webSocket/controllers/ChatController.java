package com.baraanajjar.webSocket.controllers;

import com.baraanajjar.webSocket.chat.ChatMessage;
import com.baraanajjar.webSocket.room.ChatRoomService;
import com.baraanajjar.webSocket.room.Room;
import com.baraanajjar.webSocket.room.UserUpdateMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>();

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(
            @DestinationVariable String roomId,
            @Payload ChatMessage chatMessage
    ) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage);
    }

    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(
            @DestinationVariable String roomId,
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Room room = chatRoomService.getRoomById(roomId);

        if (room != null) {
            String password = chatMessage.getPassword();
            if (room.getPassword().equals(password)) {
                String username = chatMessage.getSender();
                headerAccessor.getSessionAttributes().put("username", username);

                roomUsers.computeIfAbsent(roomId, k -> new HashSet<>()).add(username);

                UserUpdateMessage userUpdateMessage = new UserUpdateMessage(new ArrayList<>(roomUsers.get(roomId)));
                messagingTemplate.convertAndSend("/topic/users/" + roomId, userUpdateMessage);

            } else {
                ChatMessage errorMessage = new ChatMessage();
                errorMessage.setSender("System");
                errorMessage.setContent("Invalid password. You cannot join this room.");
                messagingTemplate.convertAndSend("/topic/room/" + roomId, errorMessage);
            }
        } else {
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setSender("System");
            errorMessage.setContent("Room not found: " + roomId);
            messagingTemplate.convertAndSend("/topic/room/" + roomId, errorMessage);
        }
    }



}