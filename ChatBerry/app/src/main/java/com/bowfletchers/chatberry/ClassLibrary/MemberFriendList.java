package com.bowfletchers.chatberry.ClassLibrary;

public class MemberFriendList {
    private String friendId;

    public MemberFriendList(String friendId){
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
