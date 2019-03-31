package com.bowfletchers.chatberry.ViewModel.Chat;

import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendMessage {

    private static final DatabaseReference CHAT = FirebaseDatabase.getInstance().getReference("chats");
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public void sendMessage (String id, String msg, String pfp) {

        DatabaseReference chat = CHAT.child(id);
        DatabaseReference messages = chat.child("messages");

        Message message = new Message(auth.getUid(), msg, pfp);

        //  Log.d("dname", mAuthentication.getCurrentUser().getDisplayName());

        DatabaseReference newMsg = messages.push();
        newMsg.setValue(message);
    }


}
