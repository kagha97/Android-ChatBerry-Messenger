package com.bowfletchers.chatberry.ClassLibrary;

public class GCMember {
    public String id;
    public String name;
    public String status;
    public boolean add;
    public String profilePicture;


    public GCMember (String id, String name, String status, boolean add, String profilePicture) {
        this.id = id;
        this.name = name;
        this.add = add;
        if (status.equals("0") || status.equals("")) {
            this.status = "Offline";
        }
        else {
            this.status = "Online";
        }
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getAdd() {
        return add;
    }

    public String getStatus() {
        return status;
    }

    public void Check (boolean isCheck) {
       this.add = isCheck;
    }


    public String getProfilePicture() {
        return profilePicture;
    }
}
