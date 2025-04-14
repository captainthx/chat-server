package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.ConversationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRequestRepository extends JpaRepository<ConversationRequest,Long> {
}
