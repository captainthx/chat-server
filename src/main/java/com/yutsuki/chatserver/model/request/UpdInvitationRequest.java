package com.yutsuki.chatserver.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdInvitationRequest {

    @NotNull
    @Schema(
            example = "999",
            description = "this invitationId"
    )
    private Long invitationId;


    @NotBlank
    @Schema(example = "PENDING",
            description = "this status invitation"
    )
    private String invitationStatus;
}
