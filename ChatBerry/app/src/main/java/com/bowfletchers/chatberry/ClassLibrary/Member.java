package com.bowfletchers.chatberry.ClassLibrary;

public class Member {
    private String name;
    private final int profilePicture;


    public Member (String name, int profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
    }


    public String getName () {
        return name;
    }


    public int getProfilePicture () {
        return this.profilePicture;
    }



}
