package com.baraanajjar.webSocket.repository;

import com.baraanajjar.webSocket.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppRoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
}
