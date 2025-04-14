package com.yutsuki.chatserver.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TokenResponse implements Serializable {

    private String accessToken;
    private Long accessExpire;
    private String refreshToken;
    private Long refreshExpire;
    private AccountResponse account;
}
