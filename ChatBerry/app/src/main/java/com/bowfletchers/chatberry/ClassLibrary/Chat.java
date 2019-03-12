package com.bowfletchers.chatberry.ClassLibrary;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private ArrayList<Member> memberList = new ArrayList<>();
    private ArrayList<Message> messageList = new ArrayList<>();
    private String chatTitle;

    public Chat(String ChatTitle, ArrayList<Member> member)
    {
        this.chatTitle = ChatTitle;
        this.memberList = member;
    }

    public void addMessage(Message message)
    {
        messageList.add((message));
    }

    public List<Message> getMessages() {
        return this.messageList;
    }

}
