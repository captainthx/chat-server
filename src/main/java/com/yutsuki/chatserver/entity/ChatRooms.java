package com.yutsuki.chatserver.entity;

import com.yutsuki.chatserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@ToString
@Entity
@Table(name = "chat_rooms")
public class ChatRooms extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "image_id")
    private File image;
}
