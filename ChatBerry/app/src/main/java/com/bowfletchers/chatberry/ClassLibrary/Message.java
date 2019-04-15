package com.bowfletchers.chatberry.ClassLibrary;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
@IgnoreExtraProperties
public class Message {

    public String message;
    public String senderID;
    public String imageURL;
    //private Date messageDate;

    public Message() {

    }

    public Message (String member, String message, String imageURL) {
        this.senderID = member;
        this.message = message;
        this.imageURL = imageURL;
    }

    public String getMessage() {
        return message;
    }

    public String getMember() {
        return senderID;
    }

    public String getImageURL() {
        return imageURL;
    }
}
