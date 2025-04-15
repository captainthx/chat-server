package com.yutsuki.chatserver.entity;

import com.yutsuki.chatserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room_members")
public class RoomMembers extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRooms room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "role", nullable = false)
    private String role; // 'ADMIN', 'MEMBER', 'FRIEND'
}
