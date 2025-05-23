package com.yutsuki.chatserver.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@ToString
public class UpdChatRoomRequest {

    @NotNull
    @Schema(
            example = "1",
            description = "this  chat room id from conversation"
    )
    private Long chatRoomId;

    @Size(min = 10,max = 255)
    @Schema(example = "group 1")
    private String title;

    @URL
    @Schema(
            example = "https://example.com/avatar.jpg",
            description = "URL of the avatar image from upload file api"
    )
    private String imageUrl;

}
