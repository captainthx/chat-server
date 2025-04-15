package com.yutsuki.chatserver.exception;

public class ChatRoomException extends BaseException {

    public ChatRoomException(String message) {
        super(message);
    }

  public static ChatRoomException chatRoomNameDuplicate() {
      return new ChatRoomException("chatRoomName.duplicate");
  }

  public static ChatRoomException chatRoomNotFound(){
      return new ChatRoomException("chatRoom.notFound");
    }
}
