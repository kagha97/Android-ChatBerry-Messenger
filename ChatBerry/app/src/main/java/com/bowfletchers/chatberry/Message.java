package com.bowfletchers.chatberry;

public class Message {

    private Member member;
    private String message;


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
