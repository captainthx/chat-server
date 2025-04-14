package com.yutsuki.chatserver.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileUploadResponse {

    private String name;
    private String url;
    private String type;
    private String extension;
    private long size;
}
