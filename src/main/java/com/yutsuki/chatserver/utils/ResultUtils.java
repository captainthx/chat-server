package com.yutsuki.chatserver.utils;

import com.yutsuki.chatserver.model.Result;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class ResultUtils {

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .success(true)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Result<List<T>> successEmpty() {
        return successList(Collections.emptyList(), 0L);
    }

    public static <T> Result<List<T>> successList(List<T> data) {
        return successList(data, (long) data.size());
    }

    public static <T> Result<List<T>> successList(List<T> data, Long total) {
        return Result.<List<T>>builder()
                .success(true)
                .data(data)
                .total(total)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> Result<T> successWithMessage(String message) {
        return successWithMessage(null, message);
    }

    public static <T> Result<T> successWithMessage(T data, String message) {
        return Result.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(Instant.now())
                .build();
    }
}
