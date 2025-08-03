package com.yutsuki.chatserver.controller;

import com.yutsuki.chatserver.common.BearerAuth;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.SendDirectMessageRequest;
import com.yutsuki.chatserver.model.request.SendGroupMessageRequest;
import com.yutsuki.chatserver.service.AuthService;
import com.yutsuki.chatserver.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat-message")
@RequiredArgsConstructor
@BearerAuth
@Tag(name = "Chat Message", description = "Chat Message APIs")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final AuthService authService;

    @Operation(summary = "Send Direct Message")
    @PostMapping("/send-direct")
    public Result<Void> sendDirectMessage(@RequestBody @Valid SendDirectMessageRequest request) throws BaseException {
        return chatMessageService.sendDirectMessage(request, authService.getUser());

    }

    @Operation(summary = "Send Group Message")
    @PostMapping("/send-group")
    public Result<Void> sendGroupMessage(@RequestBody @Valid SendGroupMessageRequest request) throws BaseException {
        return chatMessageService.sendGroupMessage(request, authService.getUser());
    }
}
