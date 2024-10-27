package com.baraanajjar.webSocket.room;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "app_room")
public class Room {

    @Id
    private String roomId;
    private String password;
    private String project;

    @ElementCollection
    private Set<String> participants = new HashSet<>();

    public Room(String roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }
}
