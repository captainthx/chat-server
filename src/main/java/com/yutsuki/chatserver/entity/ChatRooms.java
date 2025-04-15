package com.yutsuki.chatserver.entity;

import com.yutsuki.chatserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "chat_rooms")
public class ChatRooms extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "last_message_id")
    private SendMessage lastMessage;

    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "image_id")
    private File image;

    @OneToMany(mappedBy = "room")
    private List<RoomMembers> roomMembers;


}
