package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.SendMessage;
import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.SendMessageStatus;
import com.yutsuki.chatserver.enums.SendMessageType;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.ChatRoomException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.SendDirectMessageRequest;
import com.yutsuki.chatserver.repository.ChatRoomsRepository;
import com.yutsuki.chatserver.repository.SendMessageRepository;
import com.yutsuki.chatserver.utils.ResultUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChatMessageService {
    private final MessageService messageService;
    private final ChatRoomsRepository chatRoomsRepository;
    private final SendMessageRepository sendMessageRepository;
    private final UserService userService;

    public Result<Void> sendDirectMessage(SendDirectMessageRequest request, User user) throws BaseException {
        var recipient = userService.getUserById(request.getRecipientId());
        var optionalChatRooms = chatRoomsRepository.findById(request.getRoomId());
        if (optionalChatRooms.isEmpty()) {
            log.warn("SendDirectMessage-[block].(room not found). request:{}", request);
            throw ChatRoomException.chatRoomNotFound();
        }
        var chatRooms = optionalChatRooms.get();

        var saveMessage = messageService.saveMessage(user, chatRooms, SendMessageType.TEXT, null, null);

        // send message websocket
        messageService.sendMessageToDirectRoom(recipient, chatRooms, saveMessage);

        // update last message
        chatRooms.setLastMessage(saveMessage);
        chatRoomsRepository.save(chatRooms);
        return ResultUtils.success();
    }
}
