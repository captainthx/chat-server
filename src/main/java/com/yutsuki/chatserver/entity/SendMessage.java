package com.yutsuki.chatserver.entity;

import com.yutsuki.chatserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "send_message")
public class SendMessage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="room_id")
    private ChatRooms room;

    @ManyToOne
    @JoinColumn(name = "form_user")
    private User fromUser;

    @Column(name = "status")
    private String status;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    private String type;

}
