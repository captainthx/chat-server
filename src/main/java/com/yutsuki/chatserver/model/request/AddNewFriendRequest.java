package com.yutsuki.chatserver.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddNewFriendRequest {

    @NotNull
    @Schema(example = "999",
        description = "this userId "
    )
    private Long friendId;
}
