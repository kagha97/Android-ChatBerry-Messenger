package com.bowfletchers.chatberry.ClassLibrary;

import java.io.Serializable;

public class UserStory implements Serializable {

    public String userId;
    public String photoStoryURL;
    public String statusMessage;
    public String userName;

    public UserStory() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserStory(String userId, String userName, String photoStoryURL, String statusMessage) {
        this.userId = userId;
        this.userName = userName;
        this.photoStoryURL = photoStoryURL;
        this.statusMessage = statusMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoStoryURL() {
        return photoStoryURL;
    }

    public void setPhotoStoryURL(String photoStoryURL) {
        this.photoStoryURL = photoStoryURL;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        return "UserStory{" +
                "userId='" + userId + '\'' +
                ", photoStoryURL='" + photoStoryURL + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
