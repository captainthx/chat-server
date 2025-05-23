package com.yutsuki.chatserver.model.common;

import com.yutsuki.chatserver.enums.SendMessageType;
import com.yutsuki.chatserver.model.response.AccountResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendMessagePayload {
    private Long roomId;
    private AccountResponse sender;
    private String message;
    private String file;
    private String messageType;
}
