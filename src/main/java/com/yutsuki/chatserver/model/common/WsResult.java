package com.yutsuki.chatserver.model.common;

import com.yutsuki.chatserver.enums.ChatRoomType;
import com.yutsuki.chatserver.enums.WsEvent;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class WsResult<T> implements Serializable {

    private WsEvent event;
    private ChatRoomType type;
    private T data;
}
