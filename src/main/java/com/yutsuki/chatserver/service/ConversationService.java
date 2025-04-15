package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.repository.ConversationRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRequestRepository conversationRequestRepository;
}
