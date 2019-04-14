package com.bowfletchers.chatberry.ClassLibrary;

import java.util.List;

public class GroupChat {
   // private ArrayList<Member> memberList = new ArrayList<>();
  //  private ArrayList<Message> messageList = new ArrayList<>();
   // private String chatTitle;


    private String ownerID;
    private String name;
    private List<GCMember> MemberList;


    public GroupChat(String name, String ownerID, List<GCMember> list)
    {
        this.name = name;
        this.ownerID = ownerID;
        this.MemberList = list;

    }


    public String getOwnerID() {
        return ownerID;
    }


    public List<GCMember> getMemberList() {
        return MemberList;
    }

    public String getName() {
        return name;
    }


    public void setMemberList (List<GCMember> MemberList) {
        this.MemberList = MemberList;
    }

    public GroupChat() {

    }




}
