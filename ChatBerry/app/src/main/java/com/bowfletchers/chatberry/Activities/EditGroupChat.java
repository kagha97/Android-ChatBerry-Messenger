package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.bowfletchers.chatberry.ClassLibrary.GCMember;
import com.bowfletchers.chatberry.ClassLibrary.GroupChat;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.LiveChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class EditGroupChat extends AppCompatActivity {
    String chatID;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuthintication;
    GroupChat chat;

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_chat);
        title = findViewById(R.id.groupName);
        setTitle(getIntent().getStringExtra("title") + "'s Settings");


        chatID = getIntent().getStringExtra("id");
        title.setText(getIntent().getStringExtra("title"));
        getChatRoom(getIntent().getStringExtra("id"));





    }



    public void getChatRoom(String chatid) {
        LiveChatRoom liveChatRoom = ViewModelProviders.of(this).get(LiveChatRoom.class);
        LiveData<DataSnapshot> liveData = liveChatRoom.getGroupChatRoom(chatid);


        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
               chat = dataSnapshot.getValue(GroupChat.class);
               List<GCMember> memlist = new ArrayList<>();

                for (DataSnapshot membs : dataSnapshot.child("memberList").getChildren()) {
                    memlist.add(new GCMember(membs.child("memberID").getValue(String.class), membs.child("type").getValue(String.class)));
                }
                chat.setMemberList(memlist);




                Log.i("gcsetting", chat.getName());

                Log.i("gcsetting", String.valueOf(chat.getMemberList().size()));
            }
        });
    }





}