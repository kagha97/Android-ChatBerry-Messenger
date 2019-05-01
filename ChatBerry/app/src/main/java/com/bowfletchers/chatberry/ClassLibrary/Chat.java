package com.bowfletchers.chatberry.ClassLibrary;

import java.util.ArrayList;
import java.util.List;

public class Chat {
   // private ArrayList<Member> memberList = new ArrayList<>();
  //  private ArrayList<Message> messageList = new ArrayList<>();
   // private String chatTitle;


    public String receiverID;
    public String senderID;


    public Chat () {

    }

    public Chat(String receiverID, String senderID)
    {
        this.receiverID = receiverID;
        this.senderID = senderID;
    }

    public void addMessage(Message message)
    {
     //   messageList.add((message));
    }

    //public List<Message> getMessages() {
        //return this.messageList;
  //  }

}
