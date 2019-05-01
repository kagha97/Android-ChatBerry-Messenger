package com.bowfletchers.chatberry.ClassLibrary;

import java.io.Serializable;
import java.util.List;

public class GroupChat implements Serializable {
   // private ArrayList<Member> memberList = new ArrayList<>();
  //  private ArrayList<Message> messageList = new ArrayList<>();
   // private String chatTitle;


    public String ownerID;
    public String name;
    private List<GCMember> memberList;
    private List<Message> messages;


    public GroupChat(String name, String ownerID, List<GCMember> list)
    {
        this.name = name;
        this.ownerID = ownerID;
        this.memberList = list;

    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setName (String name) {
        this.name = name;
    }

    public List<GCMember> getMemberList() {
        return memberList;
    }

    public String getName() {
        return name;
    }


    public void setMemberList (List<GCMember> MemberList) {
        this.memberList = MemberList;
    }

    public GroupChat() {

    }




}
