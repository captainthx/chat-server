package com.yutsuki.chatserver.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetInvitationRequest extends PaginationRequest {

    @Schema(example = "PENDING")
    private String status;
}
