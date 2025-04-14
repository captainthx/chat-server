package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.SendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMessageRepository extends JpaRepository<SendMessage,Long> {
}
