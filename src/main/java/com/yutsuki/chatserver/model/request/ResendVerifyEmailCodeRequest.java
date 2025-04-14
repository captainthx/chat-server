package com.yutsuki.chatserver.model.request;

import com.yutsuki.chatserver.common.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResendVerifyEmailCodeRequest {

    @NotBlank
    @Pattern(regexp = Regex.EMAIL)
    @Schema(example = "elonmusk@email.com")
    private String email;
}
