package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.Invitations;
import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.InvitationStatus;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.ConversationException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.AddNewFriendRequest;
import com.yutsuki.chatserver.model.request.GetInvitationRequest;
import com.yutsuki.chatserver.model.request.SearchFriendRequest;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.model.response.InvitationsResponse;
import com.yutsuki.chatserver.repository.InvitationsRepository;
import com.yutsuki.chatserver.utils.ResultUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final InvitationsRepository invitationsRepository;
    private final UserService userService;


    public Result<Void> addFriend(AddNewFriendRequest request, User user) throws BaseException{
        var friend = userService.getUserById(request.getFriendId());
        if (Objects.equals(friend.getId(),user.getId())){
            log.warn("AddFriend-[block].(Unable to add myself). request:{}",request);
            throw ConversationException.UnableAddMyself();
        }
        var invitation = new Invitations();
        invitation.setSender(user);
        invitation.setRecipient(friend);
        invitation.setStatus(InvitationStatus.PENDING.name());
        invitationsRepository.save(invitation);
        return ResultUtils.successWithMessage("send invitation success.");
    }


    public Result<AccountResponse> searchFriend(SearchFriendRequest request) throws BaseException{
        var user = userService.getUserByUsername(request.getUsername());
        var response = AccountResponse.fromEntity(user);
        return ResultUtils.success(response);
    }

    public Result<List<InvitationsResponse>> getInvitationsList(GetInvitationRequest request, User user) throws BaseException{
        var search = new Invitations();
        search.setRecipient(user);
        search.setStatus(request.getStatus());

        var matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        var invitationsExample = Example.of(search, matcher);

        var pagination = request.pagination();
        var page = invitationsRepository.findAll(invitationsExample, pagination);

        var responses = page.getContent().stream().map(InvitationsResponse::fromEntity).toList();
        return ResultUtils.successList(responses,page.getTotalElements());
    }


}
