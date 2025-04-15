package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.ChatRooms;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.ChatRoomException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.UpdChatRoomRequest;
import com.yutsuki.chatserver.repository.ChatRoomsRepository;
import com.yutsuki.chatserver.repository.FileRepository;
import com.yutsuki.chatserver.utils.ResultUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomsRepository chatRoomsRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;


    public  void createChatRoom(ChatRooms request) {
            var entity = new ChatRooms();
            entity.setTitle(request.getTitle());
            entity.setType(request.getType());
        entity.setCreator(request.getCreator());
            chatRoomsRepository.save(entity);
    }


    public Result<?> updateChatRoom(UpdChatRoomRequest request) throws BaseException {
        var chatRooms = getChatRoomById(request.getChatRoomId());

        if (StringUtils.hasText(request.getTitle())){
            if (chatRoomsRepository.existsByTitle(request.getTitle())){
                log.warn("UpdateChatRoom-[block].(chatRoom title duplicate)");
                throw ChatRoomException.chatRoomNameDuplicate();
            }
            chatRooms.setTitle(request.getTitle());
        }

        if (StringUtils.hasText(request.getImageUrl())){
            var file = fileService.getByUrl(request.getImageUrl());
            if (Objects.nonNull(chatRooms.getImage()) && !request.getImageUrl().equals(chatRooms.getImage().getUrl())){
                fileService.deleteById(chatRooms.getImage().getId());
            }
            chatRooms.setImage(file);
        }
        chatRoomsRepository.save(chatRooms);
        return ResultUtils.success();
    }


    public ChatRooms getChatRoomById(Long chatRoomId) throws BaseException{
        var chatRoomsOptional = chatRoomsRepository.findById(chatRoomId);
        if( chatRoomsOptional.isEmpty()){
            log.warn("GetChatRoomById-[block].(chatRoom not found). chatRoomId:{}",chatRoomId);
            throw ChatRoomException.chatRoomNotFound();
        }
        return chatRoomsOptional.get();
    }
}
