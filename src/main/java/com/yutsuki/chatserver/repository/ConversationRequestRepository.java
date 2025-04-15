package com.yutsuki.chatserver.repository;

import com.yutsuki.chatserver.entity.ConversationInvitations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRequestRepository extends JpaRepository<ConversationInvitations,Long> {
}
