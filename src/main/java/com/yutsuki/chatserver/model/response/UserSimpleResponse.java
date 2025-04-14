package com.yutsuki.chatserver.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yutsuki.chatserver.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link User}
 */
@Getter
@Setter
@ToString
public class UserSimpleResponse implements Serializable {

    private Long id;
    private String username;
    private String avatar;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFollowing;

    public static UserSimpleResponse from(User user, Boolean isFollowing) {
        UserSimpleResponse userSimpleResponse = new UserSimpleResponse();
        userSimpleResponse.setId(user.getId());
        userSimpleResponse.setUsername(user.getUsername());

        if (Objects.nonNull(user.getAvatar())) {
            userSimpleResponse.setAvatar(user.getAvatar().getUrl());
        }

        userSimpleResponse.setIsFollowing(isFollowing);

        return userSimpleResponse;
    }
}