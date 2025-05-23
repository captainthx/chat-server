package com.yutsuki.chatserver.model.response;

import com.yutsuki.chatserver.entity.RoomMembers;
import com.yutsuki.chatserver.enums.ChatRoomType;
import com.yutsuki.chatserver.enums.RoomMembersRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoomMembersResponse implements Serializable {

    private Long id;
    private Long roomId;
    private AccountResponse user;
    private String role;
    private Boolean isAdmin;
    private Boolean canKik;

    public static RoomMembersResponse fromEntity(RoomMembers roomMembers) {
        RoomMembersResponse response = new RoomMembersResponse();
        response.setId(roomMembers.getId());
        response.setRoomId(roomMembers.getRoom().getId());
        response.setUser(AccountResponse.fromEntity(roomMembers.getUser()));
        response.setRole(roomMembers.getRole());
        if (roomMembers.getRoom().getType().equals(ChatRoomType.GROUP.name())) {
            response.setIsAdmin(RoomMembersRole.ADMIN.name().equals(roomMembers.getRole()));
            response.setCanKik(RoomMembersRole.MEMBER.name().equals(roomMembers.getRole()));
        }else {
            response.setIsAdmin(false);
            response.setCanKik(false);
        }
        return response;
    }
}
