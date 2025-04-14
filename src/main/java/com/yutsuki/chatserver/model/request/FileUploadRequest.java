package com.yutsuki.chatserver.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
public class FileUploadRequest {

    @NotNull
    private MultipartFile file;
}
