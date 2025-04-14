package com.yutsuki.chatserver.model.response;

import com.yutsuki.chatserver.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class AccountResponse implements Serializable {

    private Long id;
    private String username;
    private String email;
    private String avatar;
    private Boolean verified;

    public static AccountResponse fromEntity(User user) {
        AccountResponse response = new AccountResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setVerified(user.getVerified());

        if (Objects.nonNull(user.getAvatar())) {
            response.setAvatar(user.getAvatar().getUrl());
        }

        return response;
    }
}