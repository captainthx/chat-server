package com.yutsuki.chatserver.model.request;

import com.yutsuki.chatserver.common.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResetPasswordRequest {

    @NotNull
    @Pattern(regexp = Regex.EMAIL)
    @Schema(example = "elonmusk@email.com")
    private String email;

    @NotNull
    @Size(min = 6, max = 6)
    @Schema(example = "123456")
    private String verifyCode;

    @ToString.Exclude
    @NotNull
    @Pattern(regexp = Regex.PASSWORD)
    @Schema(example = "12345678", description = """
            <p>
            <ul>
                <li>Can contain uppercase English letters. <code>A-Z</code> (optional)</li>
                <li>Can contain lowercase English letters. <code>a-z</code> (optional)</li>
                <li>Can contain digits. <code>0-9</code> (optional)</li>
                <li>Can contain special characters. <code>#?!@$%^&*-</code> (optional)</li>
                <li>Must be between 8-30 characters long</li>
            </ul>
            </p>
            """)
    private String password;
}
