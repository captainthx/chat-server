package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms,Long> {
}
