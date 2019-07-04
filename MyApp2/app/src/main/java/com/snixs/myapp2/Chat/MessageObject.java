package com.snixs.myapp2.Chat;

public class MessageObject {

    private String messageId, senderId,senderName, message;

    public MessageObject(String messageId, String senderId, String senderName, String message){

        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {return senderName;}

    public String getMessage() {
        return message;
    }

}
