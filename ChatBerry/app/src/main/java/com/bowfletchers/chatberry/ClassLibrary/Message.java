package com.bowfletchers.chatberry.ClassLibrary;

import java.util.Date;

public class Message {

    private String member;
    private String message;
    //private Date messageDate;

    public Message (String member, String message) {
        this.member = member;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMember() {
        return member;
    }
}
