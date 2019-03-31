package com.bowfletchers.chatberry.ViewModel.Chat;

import com.bowfletchers.chatberry.ClassLibrary.Chat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewChatRoom {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chats");


    public void newChatRoom(String id) {
     /*   DatabaseReference chatDb = mDatabase;

        Chat chat = new Chat(member.id, mAuthentication.getUid());

        DatabaseReference newChat = chatDb.push();
        newChat.setValue(chat);

        chatID = newChat.getKey();*/

    }

}
