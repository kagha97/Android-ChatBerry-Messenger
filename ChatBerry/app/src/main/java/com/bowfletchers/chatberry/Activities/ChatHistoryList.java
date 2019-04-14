package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.bowfletchers.chatberry.Adapters.ChatHistoryInfoAdapter;
import com.bowfletchers.chatberry.ClassLibrary.FirebaseInstances;
import com.bowfletchers.chatberry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatHistoryList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ChatHistoryInfoAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String  logInMemberName;

    private final DatabaseReference chats = FirebaseDatabase.getInstance().getReference("chats");
    private final String auth = FirebaseAuth.getInstance().getUid();

    private ArrayList<String> chatsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseInstances.getDatabaseAuth();
        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            Intent backToSignInIntent = new Intent(ChatHistoryList.this, LoginAccount.class);
            startActivity(backToSignInIntent);
        }
        getAllChats();

        mRecyclerView = findViewById(R.id.chat_history_recycler_view);
        mAdapter = new ChatHistoryInfoAdapter( this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String userName = mUser.getDisplayName();
        if (userName != null && !userName.equals("")) {
            setTitle("Welcome " + userName);
        } else {
            setTitle("Welcome");
        }
    }

    private void getAllChats() {
        chats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsList.clear();
                for (DataSnapshot chat: dataSnapshot.getChildren()){
                    if(auth.equals(chat.child("senderId").getValue().toString()) || auth.equals(chat.child("receiverId").getValue().toString())){
                       // chatsList.add(chat.child())
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile:
                Intent userProfileIntent = new Intent(ChatHistoryList.this, UserProfile.class);
                startActivity(userProfileIntent);
                return true;
            case R.id.my_friends:
                Intent userFriendsIntent = new Intent(ChatHistoryList.this, FriendList.class);
                startActivity(userFriendsIntent);
                return true;
            case R.id.homePage:
                Intent chatListIntent = new Intent(ChatHistoryList.this, ChatHistoryList.class);
                startActivity(chatListIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
