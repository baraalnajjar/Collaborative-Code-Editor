package com.baraanajjar.webSocket.room;

import lombok.Getter;

import java.util.List;

@Getter
public class UserUpdateMessage {
    private List<String> users;

    public UserUpdateMessage(List<String> users) {
        this.users = users;
    }

}
