package com.bowfletchers.chatberry.ClassLibrary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    public String id;
    public String email;
    public String name;
    public int onlineStatus; // 0 for offline and 1 for online
    public boolean add;
    public String profilePicture;
    public List<Member> friendList;
    public List<Chat> chatList;
    public List<Story> stories;

    public Member() {

    }

    public Member (String id, String name, String email, String profilePicture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePicture = profilePicture;
        this.onlineStatus = 0;
        friendList = new ArrayList<>();
        chatList = new ArrayList<>();
        stories = new ArrayList<>();
    }

    public Member (String id, String name, String status, boolean add, String profilePicture) {
        this.id = id;
        this.name = name;
        this.add = add;
        this.onlineStatus = Integer.parseInt(status);
        this.profilePicture = profilePicture;
    }

    public Member(String name){
        this.id = "hbhbjnknk";
        this.name = name;
        this.email = "test@gmail.com";
        this.profilePicture = "";
        friendList = new ArrayList<>();
        chatList = new ArrayList<>();
        stories = new ArrayList<>();
    }




    public String getStatusString() {
        if (onlineStatus == 1) {
            return "Online";
        }
        return "Offline";
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public List<Member> getFriendList() {
        return friendList;
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void addFriend(Member friend) {
        this.friendList.add(friend);
    }

    public void removeFriend(Member friend) {
        this.friendList.remove(friend);
    }

    public void addStory(Story story) {
        this.stories.add(story);
    }

    public void removeStory(Story story) {
        this.stories.remove(story);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePicture(String newAvarta) {
        this.profilePicture = newAvarta;
    }

    public void addChat(Chat chat) {
        this.chatList.add(chat);
    }

    public void removeChat(Chat chat) {
        this.chatList.remove(chat);
    }

    public int getStatus() {
        return onlineStatus;
    }

    public void Check (boolean isCheck) {
        this.add = isCheck;
    }


    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", profilePicture=" + profilePicture +
                ", friendList=" + friendList +
                ", chatList=" + chatList +
                ", stories=" + stories +
                '}';
    }
}
