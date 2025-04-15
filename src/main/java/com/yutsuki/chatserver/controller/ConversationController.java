package com.yutsuki.chatserver.controller;

import com.yutsuki.chatserver.common.BearerAuth;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.AddNewFriendRequest;
import com.yutsuki.chatserver.model.request.GetInvitationRequest;
import com.yutsuki.chatserver.model.request.SearchFriendRequest;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.model.response.InvitationsResponse;
import com.yutsuki.chatserver.service.AuthService;
import com.yutsuki.chatserver.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/conversation")
@RequiredArgsConstructor
@BearerAuth
@Tag(name = "Conversations", description = "Conversations APIs")
public class ConversationController {

    private final ConversationService conversationService;
    private final AuthService authService;

    @Operation(summary = "Search friend")
    @PostMapping("/search-friend")
    public Result<AccountResponse> searchFriend(@RequestBody @Valid SearchFriendRequest request) throws BaseException {
        return conversationService.searchFriend(request);
    }

    @Operation(summary = "Get invitation list")
    @GetMapping("/invitation")
    public Result<List<InvitationsResponse>> getInvitationList(@ParameterObject @RequestParam GetInvitationRequest request) throws BaseException {
        return conversationService.getInvitationsList(request, authService.getUser());
    }

    @Operation(summary = "Add friend")
    @PostMapping("/new-friend")
    public Result<Void> addNewFriend(@RequestBody @Valid AddNewFriendRequest request) throws BaseException {
        return conversationService.addFriend(request, authService.getUser());
    }


}
