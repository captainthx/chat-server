package com.yutsuki.chatserver.entity;

import com.yutsuki.chatserver.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@ToString(exclude = {"password"})
@Entity(name = "users")
@Table
@DynamicInsert
@DynamicUpdate
public class User extends BaseEntity {

    @Column(name="username")
    private String username;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "phone",length = 10)
    private String phone;

    @Column(name="status")
    private String status;

    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "avatar_id")
    private File avatar;

    private Boolean verified;

    @OneToMany(mappedBy = "user")
    private List<Conversation> conversations;

    @OneToMany(mappedBy = "toUser")
    private List<ConversationRequest> receivedRequests;

}
