package com.yutsuki.chatserver.controller;

import com.yutsuki.chatserver.common.BearerAuth;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.PaginationRequest;
import com.yutsuki.chatserver.model.response.ChatRoomResponse;
import com.yutsuki.chatserver.model.response.RoomMembersResponse;
import com.yutsuki.chatserver.service.AuthService;
import com.yutsuki.chatserver.service.ChatRoomService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/chatroom")
@RequiredArgsConstructor
@BearerAuth
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final AuthService authService;


    @GetMapping
    public Result<List<ChatRoomResponse>> getChatroomList(@ParameterObject PaginationRequest request) throws BaseException {
        return chatRoomService.getChatRoomsList(request, authService.getUser())
                ;
    }

    @GetMapping("/{roomId}")
    public Result<List<RoomMembersResponse>>getRoomMemberByRoomId(@PathVariable String roomId) throws BaseException {
        return chatRoomService.getRoomsMemberByRoomId(Long.valueOf(roomId));
    }
}
