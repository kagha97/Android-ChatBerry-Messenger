package com.bowfletchers.chatberry.ClassLibrary;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String name;
    private final int profilePicture;
    private List<Member> friendList;
    private List<Chat> chatList;
    private List<Story> stories;


    public Member (String name, int profilePicture) {
        this.name = name;
        this.profilePicture = profilePicture;
        friendList = new ArrayList<>();
        chatList = new ArrayList<>();
        stories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getProfilePicture() {
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

    public void addFriend(Member friend){
        this.friendList.add(friend);
    }

    public void removeFriend(Member friend) {
        this.friendList.remove(friend);
    }
}
