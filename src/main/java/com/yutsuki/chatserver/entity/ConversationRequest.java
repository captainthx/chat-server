package com.yutsuki.chatserver.entity;

import com.yutsuki.chatserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "conversation_request")
public class ConversationRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "form_user")
    private User formUser;

    @ManyToOne
    @JoinColumn(name = "to_user")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRooms room;

    @Column(name = "status")
    private String status;
}
