package com.baraanajjar.webSocket.chat;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String password;

}