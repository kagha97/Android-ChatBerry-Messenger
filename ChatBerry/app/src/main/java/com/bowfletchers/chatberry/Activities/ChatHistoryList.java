package com.bowfletchers.chatberry.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.bowfletchers.chatberry.Adapters.ChatHistoryInfoAdapter;
import com.bowfletchers.chatberry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatHistoryList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ChatHistoryInfoAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String  logInMemberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.chat_history_recycler_view);
        mAdapter = new ChatHistoryInfoAdapter( this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        String userName = mUser.getDisplayName();
        if (userName != null && !userName.equals("")) {
            setTitle("Welcome " + userName);
        } else {
            setTitle("Welcome");
        }
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
