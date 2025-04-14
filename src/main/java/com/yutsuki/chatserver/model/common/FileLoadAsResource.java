package com.yutsuki.chatserver.model.common;

import com.yutsuki.chatserver.entity.File;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.UrlResource;

import java.io.Serializable;

@Setter
@Getter
public class FileLoadAsResource implements Serializable {

    private File file;
    private UrlResource resource;
}
