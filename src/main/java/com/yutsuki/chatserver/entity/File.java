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
@Table(name = "files")
public class File extends BaseEntity {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id",nullable = false)
    @ToString.Exclude
    private User user;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "content_type", nullable = false, length = 21)
    private String contentType;

    @Column(name = "type", nullable = false, length = 10)
    private String type;

    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Column(name = "size", nullable = false)
    private Long size;
}
