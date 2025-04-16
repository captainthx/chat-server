package com.yutsuki.chatserver.model.response;

import com.yutsuki.chatserver.entity.ChatRooms;
import com.yutsuki.chatserver.entity.Invitations;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class InvitationsResponse implements Serializable {
    private Long id;
    private AccountResponse sender;
    private String status;
    private Long roomId;
    private LocalDateTime cdt;

    public static InvitationsResponse fromEntity(Invitations invitation) {
        InvitationsResponse response = new InvitationsResponse();
        response.setId(invitation.getId());
        response.setSender(AccountResponse.fromEntity(invitation.getSender()));
        if(Objects.nonNull(invitation.getRoom())){
            response.setRoomId(invitation.getRoom().getId());
        }
        response.setStatus(invitation.getStatus());
        response.setCdt(invitation.getCdt());
        return response;
    }


}
