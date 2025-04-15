package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms, Long> {

   boolean existsByTitle(String title);
}
