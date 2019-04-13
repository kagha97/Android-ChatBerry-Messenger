package com.bowfletchers.chatberry.Activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.bowfletchers.chatberry.Adapters.MessagesAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.ClassLibrary.Member;
import com.bowfletchers.chatberry.ClassLibrary.Message;
import com.bowfletchers.chatberry.R;
import com.bowfletchers.chatberry.ViewModel.Chat.LiveChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class GroupMessageViewer extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MessagesAdapter msgAdapter;
    TextView newMessage;
    TextView tempView;
    ArrayList<Message> msgs;
    FirebaseAuth mAuthentication;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message_viewer);


        mAuthentication = FirebaseInstances.getDatabaseAuth();
        mDatabase = FirebaseInstances.getDatabaseReference("gchats");

        msgs = new ArrayList<>();
        //set up recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        msgAdapter = new MessagesAdapter(this, msgs);
        recyclerView.setAdapter(msgAdapter);

        Log.i("gchat", "chat id: " +getIntent().getStringExtra("id"));

        liveChatRoom(getIntent().getStringExtra("id"));


    }

    public void liveChatRoom(String chatid) {
        LiveChatRoom liveChatRoom = ViewModelProviders.of(this).get(LiveChatRoom.class);
        LiveData<DataSnapshot> liveData = liveChatRoom.getGroupChatRoom(chatid);


        liveData.observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                msgs.clear();
               /* for (DataSnapshot msgSnapshot: dataSnapshot.getChildren()) {

                 //   Log.i("gmessage", "Message: " + msgSnapshot.child("content").getValue(String.class) + " Sender: " + msgSnapshot.child("senderID").getValue(String.class));
                    //  Log.i(LOG_TAG, "NAME function: " + getSenderName(msgSnapshot.child("senderID").getValue(String.class)));
                    String name = "";
                    String pfp = "";

                    Log.i("gchat", "Group name: " + msgSnapshot.child("name").getValue().toString());
                    Log.i("gchat", "ID: " + msgSnapshot.child("memberList").getChildren().toString());

                  *//*  if (msgSnapshot.child("ownerID").getValue(String.class).equals(mAuthentication.getUid())) {
                        name = mAuthentication.getCurrentUser().getDisplayName();
                        pfp = mAuthentication.getCurrentUser().getPhotoUrl().toString();
                    }
                    else if (msgSnapshot.child("senderID").getValue(String.class).equals(member.getId())) {
                        name = member.getName();
                        pfp = member.getProfilePicture();
                    }*//*

                    Log.d("thename", name);

                    Log.i("IDofsender", "ID: " + msgSnapshot.child("senderID"));
                 //  Message message = new Message(name, msgSnapshot.child("content").getValue(String.class),pfp);
               //     Log.i(LOG_TAG, "MSG OBJ: " + message.getMessage());

                    //Log.i("IDofsender", "ID of object: " + message.senderID);
                   // msgs.add(message);
                }*/
                Log.i("gchat", "Group name: " + dataSnapshot.child("name").getValue().toString());


                for (DataSnapshot msgSnapshot: dataSnapshot.child("memberList").getChildren()) {
                    Log.i("gchat", "ID: " + msgSnapshot.getValue().toString());
                }

                msgAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(msgs.size() - 1);

            }
        });
    }







}
