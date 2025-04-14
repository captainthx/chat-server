package com.yutsuki.chatserver.exception;

public class UserException extends BaseException {
    public UserException(String code) {
        super(code);
    }

    public static BaseException emailDuplicate() {
        return new UserException("email.duplicate");
    }

    public static BaseException currentPasswordInvalid() {
        return new UserException("currentPassword.invalid");
    }
}
