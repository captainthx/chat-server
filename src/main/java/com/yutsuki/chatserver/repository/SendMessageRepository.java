package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.ChatRooms;
import com.yutsuki.chatserver.entity.SendMessage;
import com.yutsuki.chatserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMessageRepository extends JpaRepository<SendMessage,Long> {

Long countByRoomAndStatusAndSenderNot(ChatRooms room,String status,User sender);

}
