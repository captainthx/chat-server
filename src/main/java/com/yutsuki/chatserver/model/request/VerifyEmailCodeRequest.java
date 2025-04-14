package com.yutsuki.chatserver.model.request;

import com.yutsuki.chatserver.common.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class VerifyEmailCodeRequest {

    @NotNull
    @Pattern(regexp = Regex.EMAIL)
    @Schema(example = "elonmusk@email.com")
    private String email;

    @ToString.Exclude
    @NotNull
    @Size(min = 6, max = 6)
    @Schema(example = "123456")
    private String verifyCode;
}
