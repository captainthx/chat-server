package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.ChatRooms;
import com.yutsuki.chatserver.entity.File;
import com.yutsuki.chatserver.entity.SendMessage;
import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.ChatRoomType;
import com.yutsuki.chatserver.enums.SendMessageStatus;
import com.yutsuki.chatserver.enums.SendMessageType;
import com.yutsuki.chatserver.enums.WsEvent;
import com.yutsuki.chatserver.model.common.SendMessagePayload;
import com.yutsuki.chatserver.model.common.WsResult;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.repository.ChatRoomsRepository;
import com.yutsuki.chatserver.repository.RoomMembersRepository;
import com.yutsuki.chatserver.repository.SendMessageRepository;
import com.yutsuki.chatserver.websocket.WebSocketHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final WebSocketHandler webSocketHandler;
    private final SendMessageRepository sendMessageRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final RoomMembersRepository roomMembersRepository;

    public void sendMessageToDirectRoom(User recipient, ChatRooms room, SendMessage sendMessage) {
        // create payload data for websocket
        var payload = SendMessagePayload.builder()
                .roomId(room.getId())
                .sender(AccountResponse.fromEntity(sendMessage.getSender()))
                .message(sendMessage.getMessage())
                .messageType(sendMessage.getType())
                .file(sendMessage.getAttachment() != null ? sendMessage.getAttachment().getUrl() : null)
                .build();

        // set payload websocket
        var wsResult = WsResult.builder()
                .event(WsEvent.SEND_MESSAGE)
                .type(ChatRoomType.DIRECT)
                .data(payload)
                .build();

        //send to websocket
        webSocketHandler.push(wsResult, recipient.getId());
    }


    public void sendMessageToGroupRoom(User sender, ChatRooms room, SendMessage sendMessage) {
        var rooms = roomMembersRepository.findByRoomId(room.getId());
        var userIds = rooms.stream().map(e -> e.getUser().getId()).toList();
        log.info("send to group room userIds {}", userIds);

        // create payload data for websocket
        var payload = SendMessagePayload.builder()
                .roomId(room.getId())
                .sender(AccountResponse.fromEntity(sender))
                .message(sendMessage.getMessage())
                .messageType(sendMessage.getType())
                .file(sendMessage.getAttachment() != null ? sendMessage.getAttachment().getUrl() : null)
                .build();

        // set payload websocket
        var wsResult = WsResult.builder()
                .event(WsEvent.SEND_MESSAGE)
                .type(ChatRoomType.GROUP)
                .data(payload)
                .build();
        //send to websocket
        webSocketHandler.pushToGroup(wsResult, userIds);
    }


    public SendMessage saveMessage(User sender,
                                   ChatRooms room,
                                   SendMessageType messageType,
                                   File file,
                                   SendMessage replyTo
    ) {
        var sendMessage = new SendMessage();
        sendMessage.setSender(sender);
        sendMessage.setRoom(room);
        sendMessage.setStatus(SendMessageStatus.UNREAD.name());
        sendMessage.setType(messageType.name());
        sendMessage.setAttachment(file);
        sendMessage.setReplyTo(replyTo);
        return sendMessageRepository.save(sendMessage);
    }

}
