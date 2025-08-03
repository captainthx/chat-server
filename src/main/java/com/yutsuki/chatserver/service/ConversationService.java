package com.yutsuki.chatserver.service;

import com.yutsuki.chatserver.entity.ChatRooms;
import com.yutsuki.chatserver.entity.Invitations;
import com.yutsuki.chatserver.entity.RoomMembers;
import com.yutsuki.chatserver.entity.User;
import com.yutsuki.chatserver.enums.ChatRoomType;
import com.yutsuki.chatserver.enums.InvitationStatus;
import com.yutsuki.chatserver.enums.RoomMembersRole;
import com.yutsuki.chatserver.exception.BaseException;
import com.yutsuki.chatserver.exception.ConversationException;
import com.yutsuki.chatserver.model.Result;
import com.yutsuki.chatserver.model.request.AddNewFriendRequest;
import com.yutsuki.chatserver.model.request.GetInvitationRequest;
import com.yutsuki.chatserver.model.request.SearchFriendRequest;
import com.yutsuki.chatserver.model.request.UpdInvitationRequest;
import com.yutsuki.chatserver.model.response.AccountResponse;
import com.yutsuki.chatserver.model.response.InvitationsResponse;
import com.yutsuki.chatserver.repository.ChatRoomsRepository;
import com.yutsuki.chatserver.repository.InvitationsRepository;
import com.yutsuki.chatserver.repository.RoomMembersRepository;
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
    private final ChatRoomsRepository chatRoomsRepository;
    private final RoomMembersRepository roomMembersRepository;


    public Result<Void> addFriend(AddNewFriendRequest request, User user) throws BaseException {
        var friend = userService.getUserById(request.getFriendId());
        if (Objects.equals(friend.getId(), user.getId())) {
            log.warn("AddFriend-[block].(Unable to add myself). request:{}", request);
            throw ConversationException.UnableAddMyself();
        }
        var invitation = new Invitations();
        invitation.setSender(user);
        invitation.setRecipient(friend);
        invitation.setStatus(InvitationStatus.PENDING.name());
        invitationsRepository.save(invitation);
        return ResultUtils.successWithMessage("send invitation success.");
    }


    public Result<AccountResponse> searchFriend(SearchFriendRequest request) throws BaseException {
        var user = userService.getUserByUsername(request.getUsername());
        var response = AccountResponse.fromEntity(user);
        return ResultUtils.success(response);
    }

    public Result<List<InvitationsResponse>> getInvitationsList(GetInvitationRequest request, User user) throws BaseException {
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

        var responses = page.map(InvitationsResponse::fromEntity).toList();
        return ResultUtils.successList(responses, page.getTotalElements());
    }

    public Result<Void> acceptInvitation(UpdInvitationRequest request, User user) throws BaseException {
        var invitationsOptional = invitationsRepository.findById(request.getInvitationId());
        if (invitationsOptional.isEmpty()) {
            log.warn("UpdateInvitation-[block].(invitation not found), request:{}", request);
            throw ConversationException.InvitationNotFound();
        }
        var invitations = invitationsOptional.get();
        if (!invitations.getStatus().equals(InvitationStatus.PENDING.name())) {
            log.warn("UpdateInvitation-[block].(invalid invitation). request:{}", request);
            throw ConversationException.InvitationInValid();
        }
        if (request.getInvitationStatus().equals(InvitationStatus.ACCEPT.name())) {
            var chatRooms = new ChatRooms();
            chatRooms.setCreator(invitations.getSender());
            chatRooms.setType(ChatRoomType.DIRECT.name());
            chatRoomsRepository.save(chatRooms);

            var userList = List.of(invitations.getSender(), user);
            addRoomMember(chatRooms,userList);

            invitations.setStatus(InvitationStatus.ACCEPT.name());
            invitationsRepository.save(invitations);

        } else if ((request.getInvitationStatus().equals(InvitationStatus.REJECT.name()))) {
            invitations.setStatus(InvitationStatus.REJECT.name());
            invitationsRepository.save(invitations);
        }
        return ResultUtils.success();
    }


    public void addRoomMember(ChatRooms chatRooms, List<User> users) {
        var members = users.stream().map(user -> {
            var roomMembers = new RoomMembers();
            roomMembers.setRoom(chatRooms);
            roomMembers.setRole(RoomMembersRole.FRIEND.name());
            roomMembers.setUser(user);
            return roomMembers;
        }).toList();
        roomMembersRepository.saveAll(members);
    }
}
