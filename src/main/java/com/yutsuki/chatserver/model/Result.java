package com.yutsuki.chatserver.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Getter
public class Result<T> implements Serializable {

    private boolean success;
    private T data;
    private String error;
    private String message;
    private Long total;

    @Builder.Default
    private Instant timestamp = Instant.now();
}
