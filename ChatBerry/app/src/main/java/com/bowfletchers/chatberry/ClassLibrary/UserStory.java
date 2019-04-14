package com.bowfletchers.chatberry.ClassLibrary;

public class UserStory {

    public String userId;
    public String photoStoryURL;
    public String statusMessage;

    public UserStory() {

    }

    public UserStory(String userId, String photoStoryURL, String statusMessage) {
        this.userId = userId;
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
