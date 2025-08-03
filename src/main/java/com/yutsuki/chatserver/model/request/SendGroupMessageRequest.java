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
public class SendGroupMessageRequest {

    @NotNull
    @Schema(example = "999")
    private Long roomId;

    @NotBlank
    @Schema(example = "hi john")
    private String message;

    @Schema(example = "http://12312321/addasdasd")
    private String attachmentUrl;
}
