package com.bowfletchers.chatberry.ClassLibrary;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
@IgnoreExtraProperties
public class Message {

    public String content;
    public String senderID;
    //private Date messageDate;

    public Message() {

    }

    public Message (String member, String message) {
        this.senderID = member;
        this.content = message;
    }

    public String getMessage() {
        return content;
    }

    public String getMember() {
        return senderID;
    }
}
