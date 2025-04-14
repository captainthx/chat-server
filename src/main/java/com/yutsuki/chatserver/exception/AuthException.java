package com.yutsuki.chatserver.exception;

public class AuthException extends BaseException {

    public AuthException(String code) {
        super(code);
    }

    public static AuthException unauthorized() {
        return new AuthException("unauthorized");
    }

    public static AuthException credentialsInvalid() {
        return new AuthException("credentials.invalid");
    }

    public static AuthException userNotFound() {
        return new AuthException("user.notFound");
    }

    public static AuthException verifyCodeInvalid() {
        return new AuthException("verifyCode.invalid");
    }
}