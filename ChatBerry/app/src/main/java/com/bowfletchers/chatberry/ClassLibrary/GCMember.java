package com.bowfletchers.chatberry.ClassLibrary;

public class GCMember {


    private String memberID;
    private int type;


    public GCMember (String id, int type) {
        this.memberID = id;
        this.type = type;
    }


    public String getMemberID() {
        return memberID;
    }

    public int getType() {
        return type;
    }

}
