package com.bowfletchers.chatberry.ClassLibrary;

import java.util.Date;

public class Message {

    private Member member;
    private String message;
    //private Date messageDate;

    public Message (Member member, String message) {
        this.member = member;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Member getMember() {
        return member;
    }
}
