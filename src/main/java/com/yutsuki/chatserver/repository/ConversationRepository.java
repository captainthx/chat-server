package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
