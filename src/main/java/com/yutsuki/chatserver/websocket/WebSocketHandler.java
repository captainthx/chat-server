package com.yutsuki.chatserver.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yutsuki.chatserver.model.common.WsResult;
import com.yutsuki.chatserver.utils.LocalDateTimeAdapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@Log4j2
@Service
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var principal = session.getPrincipal();
        if (principal == null || principal.getName() == null) {
            session.close(SERVER_ERROR.withReason("User must be authenticated"));
            return;
        }

        sessions.putIfAbsent(principal.getName(), session);
        session.sendMessage(new TextMessage("Connected"));
        log.debug("Websocket-[connected]. ==> principal {}", principal.getName());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var principal = session.getPrincipal();

        if (principal != null && principal.getName() != null) {
            log.debug("Websocket-[disconnected]. ==> principal {}", principal.getName());
            sessions.remove(principal.getName());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message.getPayload().equals("PING")) {
            session.sendMessage(new TextMessage("PONG"));
        }
    }

    public <T> void push(WsResult<T> result, Long userId) {
        var session = sessions.get(String.valueOf(userId));
        log.debug("Push-[start] userId:{}", userId);
        if (session != null && session.isOpen()) {
            try {
                var json = gson.toJson(result);
                log.debug("Push-[next]. userId:{}, json:{}", userId, json);
                session.sendMessage(new TextMessage(json));
            } catch (Exception e) {
                log.warn("Push-[block]. userId:{}, error:{}", userId, e.getMessage());
            }
        } else {
            log.debug("Push-[no-session]. userId:{} ", userId);
        }
    }

    public <T> void pushToGroup(WsResult<T> result, List<Long> userIds) {
        log.debug("PushToGroup-[start] userIds:{}", userIds);
        for (Long userId : userIds) {
            push(result, userId);
        }
    }
}
