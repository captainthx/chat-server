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
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "status")
    private String status; // 'READ','UNREAD'

    @Column(name = "message",columnDefinition = "TEXT")
    private String message;

    @Column(name = "type")
    private String type; // 'FILE','TEXT'

    @ManyToOne
    @JoinColumn(name = "attachment_id")
    private File attachment;

    @ManyToOne
    @JoinColumn(name = "reply_to_id")
    private SendMessage replyTo;

}
