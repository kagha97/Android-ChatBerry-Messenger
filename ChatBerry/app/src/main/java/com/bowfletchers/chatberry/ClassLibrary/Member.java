package com.bowfletchers.chatberry.ClassLibrary;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String name;
    private int profilePicture;
    private List<Member> friendList;
    private List<Chat> chatList;
    private List<Story> stories;


    public Member (String name) {
        this.name = name;
        this.profilePicture = 0;
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

    public void setProfilePicture(int newAvarta) {
        this.profilePicture = newAvarta;
    }

    public void addChat(Chat chat) {
        this.chatList.add(chat);
    }

    public void removeChat(Chat chat) {
        this.chatList.remove(chat);
    }
}
