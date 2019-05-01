package com.bowfletchers.chatberry.ClassLibrary;

public class Invitations {
    public String senderId;
    public String receiverId;

    public Invitations(String SenderId, String ReceiverId) {
        this.senderId = SenderId;
        this.receiverId = ReceiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

}

