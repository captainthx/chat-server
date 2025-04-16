package com.yutsuki.chatserver.exception;

public class ConversationException extends BaseException {

    public ConversationException(String message) {
        super(message);
    }


    public static BaseException UnableAddMyself() {
        return new ConversationException("unable.addMyself");
    }

    public static ConversationException InvitationNotFound() {
        return new ConversationException("invitation.notFound");
    }

    public static ConversationException InvitationInValid() {
        return new ConversationException("invitation.invalid");
    }

}
