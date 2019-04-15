package com.bowfletchers.chatberry.ClassLibrary;

import java.io.Serializable;

public class GCMember implements Serializable {


    private String memberID;
    private String type;


    public GCMember (String id, String type) {
        this.memberID = id;
        this.type = type;
    }


    public GCMember() {

    }

    public String getMemberID() {
        return memberID;
    }

    public String getType() {
        return type;
    }

}
