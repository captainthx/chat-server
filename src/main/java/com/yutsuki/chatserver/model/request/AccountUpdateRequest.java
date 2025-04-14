package com.yutsuki.chatserver.model.request;

import com.yutsuki.chatserver.common.Regex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@Setter
@Getter
@ToString
public class AccountUpdateRequest {

    @Pattern(regexp = Regex.USERNAME)
    @Schema(
            example = "username123",
            description = """
                    <p>
                    <ul>
                        <li>Must start with an letter <code>A-Z</code> or a digit <code>0-9</code></li>
                        <li>Can contain uppercase <code>A-Z</code>, lowercase <code>a-z</code> letters, digits <code>0-9</code>, periods <code>.</code>, and underscores <code>_</code></li>
                        <li>Cannot contain consecutive periods <code>..</code>, e.g. <code>user..name</code></li>
                        <li>Cannot end with a period <code>.</code>, e.g. <code>username.</code></li>
                        <li>Must be between 1-30 characters long</li>
                    </ul>
                    </p>
                    """
    )
    private String username;

    @URL
    @Schema(
            example = "https://example.com/avatar.jpg",
            description = "URL of the avatar image from upload file api"
    )
    private String avatar;
}
