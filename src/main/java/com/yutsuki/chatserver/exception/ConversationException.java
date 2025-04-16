package com.yutsuki.chatserver.exception;

public class ConversationException extends BaseException {

    public ConversationException(String message) {
        super(message);
    }


    public static BaseException UnableAddMyself() {
        return new ConversationException("unable.addMyself");
    }
}
