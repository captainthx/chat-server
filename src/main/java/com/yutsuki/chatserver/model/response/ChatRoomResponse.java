package com.yutsuki.chatserver.model.response;

import com.yutsuki.chatserver.entity.ChatRooms;
import com.yutsuki.chatserver.enums.ChatRoomType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ChatRoomResponse implements Serializable {

    private Long roomId;
    private String title;
    private String type;
    private String image;
    private List<AccountResponse> members;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Long unreadCount;


    public static ChatRoomResponse fromEntity(ChatRooms chatRooms,Long unreadCount) {

        var members = chatRooms.getRoomMembers()
                .stream()
                .filter(m -> !m.getUser().equals(chatRooms.getCreator()))
                .findFirst().orElseThrow().getUser();

        ChatRoomResponse response = new ChatRoomResponse();
        response.setRoomId(chatRooms.getId());
        if (chatRooms.getType().equals(ChatRoomType.DIRECT.name())) {
            response.setTitle(members.getUsername());
            if (Objects.nonNull(members.getAvatar())){
            response.setImage(members.getAvatar().getUrl());
            }
        } else if (chatRooms.getType().equals(ChatRoomType.GROUP.name())) {
            response.setTitle(chatRooms.getTitle());
            response.setImage(chatRooms.getImage().getUrl());
        }
        response.setType(chatRooms.getType());
        response.setMembers(chatRooms.getRoomMembers().stream()
                .map(roomMembers -> AccountResponse.fromEntity(roomMembers.getUser())).toList());
        if (Objects.nonNull(chatRooms.getLastMessage())){
            response.setLastMessage(chatRooms.getLastMessage().getMessage());
            response.setLastMessageTime(chatRooms.getLastMessage().getCdt());
        }
        response.setUnreadCount(unreadCount);
        return response;
    }
}
